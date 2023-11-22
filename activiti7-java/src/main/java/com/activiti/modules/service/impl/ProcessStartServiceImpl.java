package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.vo.workflow.HighlightNodeInfoVo;
import com.activiti.modules.entity.vo.workflow.HistoryRecordIdentityVo;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.modules.entity.vo.workflow.StartListVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.modules.service.SysDeptService;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.constant.ActivityType;
import com.activiti.utils.constant.Constant;
import com.activiti.utils.constant.HighlightNodeStatus;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流程启动
 *
 * @author liuguofeng
 * @date 2023/11/04 12:09
 **/
@Service("processStartService")
public class ProcessStartServiceImpl implements ProcessStartService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @Autowired
    private SysDeptService deptService;

    @Autowired
    private SysUserService userService;

    /**
     * 我发起的任务列表
     *
     * @param dto 参数
     * @return 结果
     */
    @Override
    public TableDataInfo queryPage(StartListDto dto) {
        PageDomain params = PageUtils.getPageParams();

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .startedBy(dto.getUserId())
                .orderByProcessInstanceStartTime()
                .desc();

        // 根据流程名称查询 注意是等于不是模糊查询
        if (StringUtils.isNoneEmpty(dto.getDefinitionName())) {
            query.processDefinitionName(dto.getDefinitionName());
        }
        // 根据流程key查询 注意是等于不是模糊查询
        if (StringUtils.isNoneEmpty(dto.getDefinitionKey())) {
            query.processDefinitionKey(dto.getDefinitionKey());
        }

        List<HistoricProcessInstance> list = query
                .listPage(params.getPageNo() - 1, params.getPageSize());
        List<StartListVo> resultList = new ArrayList<>();
        for (HistoricProcessInstance item : list) {
            // 设置流程实例
            StartListVo vo = new StartListVo();
            vo.setId(item.getId());
            vo.setStartTime(item.getStartTime());
            vo.setEndTime(item.getEndTime());

            // 流程定义信息
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(item.getProcessDefinitionId())
                    .singleResult();
            vo.setDefinitionName(definition.getName());
            vo.setDefinitionKey(definition.getKey());
            vo.setDefinitionVersion(definition.getVersion());

            // 获取任务处理节点
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(item.getId())
                    .list();
            if (taskList.size() != 0) {
                Task task = taskList.get(0);
                vo.setTaskId(task.getId());
                vo.setTaskName(task.getName());
                vo.setStatus(1);
            } else {
                // 任务处理完成在history获取
                List<HistoricTaskInstance> historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(item.getId())
                        .orderByHistoricTaskInstanceEndTime()
                        .desc()
                        .list();
                HistoricTaskInstance task = historicTaskInstance.get(0);
                vo.setTaskId(task.getId());
                vo.setTaskName(task.getName());
                vo.setStatus(2);
            }
            resultList.add(vo);
        }
        return PageUtils.getDataTable(resultList, query.count());
    }

    /**
     * 启动流程
     *
     * @param definitionId 流程定义id
     * @param userId       当前用户登录id
     */
    @Override
    public void startProcess(String definitionId, String userId) {
        // 设置流程发起人用户Id
        Authentication.setAuthenticatedUserId(userId);
        Map<String, Object> variables = new HashMap<>();
        variables.put(Constant.PROCESS_INITIATOR, userId);
        runtimeService.startProcessInstanceById(definitionId, variables);
    }

    /**
     * 查询审批进度
     *
     * @param instanceId 流程实例id
     * @return 审批记录
     */
    @Override
    public List<HistoryRecordVo> getHistoryRecord(String instanceId) {
        if (StringUtils.isEmpty(instanceId)) throw new AException("instanceId不能为空!");
        List<HistoryRecordVo> resultList = new ArrayList<>();
        // 已审批审批节点
        List<HistoricActivityInstance> executedList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        for (HistoricActivityInstance item : executedList) {
            // 除了用户节点以外的节点都过滤掉
            if (!ActivityType.USER_TASK.equals(item.getActivityType())) continue;
            HistoryRecordVo vo = new HistoryRecordVo();
            vo.setNodeName(item.getActivityName());
            vo.setStartTime(item.getStartTime());
            vo.setEndTime(item.getEndTime());
            vo.setUserId(item.getAssignee());
            // 审批意见
            taskService.getTaskComments(item.getTaskId())
                    .stream()
                    .findAny()
                    .ifPresent(t -> vo.setComment(t.getFullMessage()));
            // 审批人
            if (StringUtils.isNotEmpty(item.getAssignee())) {
                SysUserEntity user = userService.getById(item.getAssignee());
                if (user != null) {
                    vo.setUserName(user.getUsername());
                }
            }
            // 获取获选人 或 候选组信息
            vo.setIdentity(getCandidateInfo(item.getTaskId()));
            vo.setStatus(1);
            resultList.add(vo);
        }

        // 获取未审批节点(活动的待审批(下一个待审批节点))
        List<HistoricActivityInstance> unfinishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();
        for (HistoricActivityInstance item : unfinishedList) {
            HistoryRecordVo vo = new HistoryRecordVo();
            vo.setNodeName(item.getActivityName());
            // 审批人
            if (StringUtils.isNotEmpty(item.getAssignee())) {
                SysUserEntity user = userService.getById(item.getAssignee());
                if (user != null) {
                    vo.setUserName(user.getUsername());
                }
            }
            // 获取获选人 或 候选组信息
            vo.setIdentity(getCandidateInfo(item.getTaskId()));
            vo.setStatus(2);
            resultList.add(vo);
        }
        return resultList;
    }

    /**
     * 查询流程图信息(高亮信息)
     *
     * @param instanceId 流程实例id
     * @return 流程图信息
     */
    @Override
    public Map<String, Object> getHighlightNodeInfo(String instanceId) {
        Map<String, Object> result = new HashMap<>();
        // 流程实例记录
        HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        if (historicInstance == null) {
            throw new AException("未知流程");
        }
        // 获取bpmn流程图
        String xml = processDefinitionService.getDefinitionXml(historicInstance.getDeploymentId());
        result.put("xml", xml);

        // 高亮节点信息
        List<HighlightNodeInfoVo> nodeInfo = new ArrayList<>();
        // 已审批审批节点
        List<HistoricActivityInstance> executedList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        executedList.forEach(item -> {
            nodeInfo.add(new HighlightNodeInfoVo() {{
                setActivityId(item.getActivityId());
                setStatus(HighlightNodeStatus.EXECUTED);
            }});
        });

        // 获取未审批节点(活动的待审批(下一个待审批节点))
        List<HistoricActivityInstance> unfinishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();
        unfinishedList.forEach(item -> {
            nodeInfo.add(new HighlightNodeInfoVo() {{
                setActivityId(item.getActivityId());
                setStatus(HighlightNodeStatus.UNFINISHED);
            }});
        });

        // 获取流程图图像字符流
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicInstance.getProcessDefinitionId());
        // 添加已经流转和活动待审批的线
        nodeInfo.addAll(getFlowsStatus(bpmnModel, executedList, unfinishedList));

        result.put("nodeInfo", nodeInfo);
        return result;
    }


    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例id
     */
    @Override
    public void delete(String instanceId) {
        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(instanceId, null);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(instanceId);
    }

    /**
     * 获取获选人 或 候选组
     *
     * @param taskId 任务id
     * @return 获选人 或 候选组信息
     */
    public HistoryRecordIdentityVo getCandidateInfo(String taskId) {
        HistoryRecordIdentityVo vo = new HistoryRecordIdentityVo();
        // 如果没有taskId直接返回
        if (StringUtils.isEmpty(taskId)) return vo;
        List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(taskId);

        // 候选组ids
        List<String> groupIds = identityLinks.stream()
                .filter(t -> t.getType().equals(IdentityLinkType.CANDIDATE))
                .map(HistoricIdentityLink::getGroupId)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        // 查询数据库找到候选组名称(部门名称)
        if (groupIds.size() != 0) {
            List<String> groupNames = deptService.list(new LambdaQueryWrapper<SysDeptEntity>()
                            .in(SysDeptEntity::getDeptId, groupIds))
                    .stream().map(SysDeptEntity::getDeptName).collect(Collectors.toList());
            vo.setGroupNames(groupNames);
        }
        // 候选人ids
        List<String> userIds = identityLinks.stream()
                .filter(t -> t.getType().equals(IdentityLinkType.CANDIDATE))
                .map(HistoricIdentityLink::getUserId)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (userIds.size() != 0) {
            List<String> userNames = userService.list(new LambdaQueryWrapper<SysUserEntity>()
                            .in(SysUserEntity::getUserId, userIds))
                    .stream().map(SysUserEntity::getUsername).collect(Collectors.toList());
            vo.setUserNames(userNames);
        }
        return vo;
    }

    /**
     * 获取已经流转和活动待审批的线
     *
     * @param bpmnModel      bpmn模型
     * @param executedList   已审批审批节点
     * @param unfinishedList 活动待审批节点
     */
    private List<HighlightNodeInfoVo> getFlowsStatus(BpmnModel bpmnModel,
                                                     List<HistoricActivityInstance> executedList,
                                                     List<HistoricActivityInstance> unfinishedList) {
        List<HighlightNodeInfoVo> resultList = new ArrayList<>();
        //todo 流程图显示还有些问题
        // 假设只有开始和结束和三条线，每一条线上有流程变量 a=1，a<1, a>1,如果满足其中a=1,程序则在流程图高亮显示，三条线都高亮

        // -------------                           ------------
        // |绿色已审批节点| ------绿色已审批线------>  |绿色已审批节点|
        // -------------                           ------------
        // 获取'绿色已审批线'-------------
        // 先找到流入到 ‘已审批审批节点’ 的线
        for (HistoricActivityInstance item : executedList) {
            List<SequenceFlow> sequenceFlows = getSequenceFlowsByActivityId(bpmnModel, item.getActivityId());
            sequenceFlows.forEach(flow -> {
                // 排除sourceRef,如果 sourceRef 不包含 ‘活动待审批节点’ 中则排除
                String sourceRef = flow.getSourceRef();
                long count = executedList.stream().filter(t -> t.getActivityId().equals(sourceRef)).count();
                if (count != 0) {
                    resultList.add(new HighlightNodeInfoVo() {{
                        setActivityId(flow.getId());
                        setStatus(HighlightNodeStatus.EXECUTED);
                    }});
                }
            });
        }
        // 获取'绿色已审批线'-------------


        // -------------                           ------------
        // |绿色已审批节点| ------黄色待审批线------>  |黄色待审批节点|
        // -------------                           ------------
        // 获取'黄色待审批线'-------------
        for (HistoricActivityInstance item : unfinishedList) {
            List<SequenceFlow> sequenceFlows = getSequenceFlowsByActivityId(bpmnModel, item.getActivityId());
            sequenceFlows.forEach(flow -> {
                // 排除sourceRef,如果 sourceRef 不包含 ‘活动待审批节点’ 中则排除
                String sourceRef = flow.getSourceRef();
                long count = executedList.stream().filter(t -> t.getActivityId().equals(sourceRef)).count();
                if (count != 0) {
                    resultList.add(new HighlightNodeInfoVo() {{
                        setActivityId(flow.getId());
                        setStatus(HighlightNodeStatus.UNFINISHED);
                    }});
                }
            });
        }
        // 获取'黄色待审批线'-------------
        return resultList;
    }

    /**
     * 获取节点输入的线
     *
     * @param bpmnModel  bpmn模型
     * @param activityId 节点id
     * @return 线list
     */
    private List<SequenceFlow> getSequenceFlowsByActivityId(BpmnModel bpmnModel, String activityId) {
        // 获取到当前节点
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
                .getFlowElement(activityId, true);
        // 获取到流入的线
        return flowNode.getIncomingFlows();
    }

}
