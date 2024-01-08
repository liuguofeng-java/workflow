package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.FinishedListDto;
import com.activiti.modules.entity.vo.workflow.FinishedListVo;
import com.activiti.modules.entity.vo.workflow.HighlightNodeInfoVo;
import com.activiti.modules.entity.vo.workflow.IdentityVo;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.modules.service.*;
import com.activiti.utils.constant.ActivityType;
import com.activiti.utils.constant.NodeStatus;
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
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 流程历史相关
 *
 * @author liuguofeng
 * @date 2023/11/24 09:40
 **/
@Service("processHistoryService")
public class ProcessHistoryServiceImpl implements ProcessHistoryService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @Autowired
    private SysDeptService deptService;

    @Autowired
    private SysUserService userService;

    /**
     * 已办任务列表
     *
     * @param dto 参数
     * @return 结果
     */
    @Override
    public TableDataInfo queryPage(FinishedListDto dto) {
        PageDomain params = PageUtils.getPageParams();

        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskAssignee(dto.getUserId())
                .orderByHistoricTaskInstanceEndTime()
                .desc();

        // 根据流程名称查询 注意是等于不是模糊查询
        if (StringUtils.isNoneEmpty(dto.getDefinitionName())) {
            query.processDefinitionName(dto.getDefinitionName());
        }
        // 根据流程key查询 注意是等于不是模糊查询
        if (StringUtils.isNoneEmpty(dto.getDefinitionKey())) {
            query.processDefinitionKey(dto.getDefinitionKey());
        }
        List<HistoricTaskInstance> historicTasks =
                query.listPage(params.getPageNo() - 1, params.getPageSize());
        List<FinishedListVo> list = new ArrayList<>();
        for (HistoricTaskInstance historicTask : historicTasks) {
            FinishedListVo vo = new FinishedListVo();
            // 当前流程
            vo.setTaskId(historicTask.getId());
            vo.setTaskName(historicTask.getName());
            vo.setTaskDefinitionKey(historicTask.getTaskDefinitionKey());
            vo.setProcessInstanceId(historicTask.getProcessInstanceId());
            vo.setCreateTime(historicTask.getCreateTime());
            vo.setProcessDefinitionId(historicTask.getProcessDefinitionId());

            // 流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(historicTask.getProcessDefinitionId())
                    .singleResult();
            vo.setDefinitionName(definition.getName());
            vo.setDefinitionKey(definition.getKey());
            vo.setDefinitionVersion(definition.getVersion());

            // 流程发起人
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(historicTask.getProcessInstanceId())
                    .singleResult();
            SysUserEntity user = userService.getById((instance.getStartUserId()));
            vo.setStartUserId(user.getUserId());
            vo.setStartUserName(user.getUsername());
            list.add(vo);
        }
        return PageUtils.getDataTable(list, query.count());
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
        // 当前流程的流程变量
        List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(instanceId)
                .list();

        // 已审批审批节点
        List<HistoricActivityInstance> finishedList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();
        for (HistoricActivityInstance item : finishedList) {
            // 除了用户节点以外的节点都过滤掉
            if (!ActivityType.USER_TASK.equals(item.getActivityType())) continue;
            HistoryRecordVo vo = new HistoryRecordVo();
            vo.setNodeName(item.getActivityName());
            vo.setActivityId(item.getActivityId());
            vo.setStartTime(item.getStartTime());
            vo.setEndTime(item.getEndTime());
            // 获取获选人 或 候选组信息
            vo.setIdentity(getCandidateInfo(item.getTaskId()));
            vo.setStatus(NodeStatus.EXECUTED);

            // 设置流程变量,根据activityId回显动态表单(用户提交的数据),因为前端赋值是这样的:
            // variables[`${activityId}_formJson`] = formJson;
            // variables[`${activityId}_formData`] = JSON.parse(JSON.stringify(formData));
            historicVariables.stream()
                    .filter(t -> t.getVariableName().equals(String.format("%s_formJson", item.getActivityId()))
                            && item.getTaskId().equals(t.getTaskId()))
                    .findAny()
                    .ifPresent(t -> vo.setFormJson(t.getValue()));

            historicVariables.stream()
                    .filter(t -> t.getVariableName().equals(String.format("%s_formData", item.getActivityId()))
                            && item.getTaskId().equals(t.getTaskId()))
                    .findAny()
                    .ifPresent(t -> vo.setFormData(t.getValue()));

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
            vo.setActivityId(item.getActivityId());
            // 获取获选人 或 候选组信息
            vo.setIdentity(getCandidateInfo(item.getTaskId()));
            vo.setStatus(NodeStatus.UNFINISHED);
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

        // 历史审批记录
        List<HistoryRecordVo> historyList = getHistoryRecord(instanceId);

        // 高亮节点信息
        List<HighlightNodeInfoVo> nodeInfo = new ArrayList<>();
        // 全部审批节点
        List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId).finished()
                .orderByHistoricActivityInstanceId().asc()
                .list();
        finishedList.forEach(item -> {
            // 节点详情
            List<HistoryRecordVo> nodeHistory = historyList.stream()
                    .filter(t -> t.getActivityId().equals(item.getActivityId()))
                    .collect(Collectors.toList());
            nodeInfo.add(new HighlightNodeInfoVo() {{
                setActivityId(item.getActivityId());
                setStatus(NodeStatus.EXECUTED);
                setHistoryRecordVo(nodeHistory);
            }});
        });

        // 获取未审批节点(活动的待审批(下一个待审批节点))
        List<HistoricActivityInstance> unfinishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId).unfinished()
                .orderByHistoricActivityInstanceId().asc()
                .list();
        unfinishedList.forEach(item -> {
            // 节点详情
            List<HistoryRecordVo> nodeHistory = historyList.stream()
                    .filter(t -> t.getActivityId().equals(item.getActivityId()))
                    .collect(Collectors.toList());
            nodeInfo.add(new HighlightNodeInfoVo() {{
                setActivityId(item.getActivityId());
                setStatus(NodeStatus.UNFINISHED);
                setHistoryRecordVo(nodeHistory);
            }});
        });

        // 获取流程图图像字符流
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicInstance.getProcessDefinitionId());
        // 添加已经流转和活动待审批的线
        nodeInfo.addAll(getHighLightedFlows(bpmnModel, finishedList, unfinishedList));

        result.put("nodeInfo", nodeInfo);
        return result;
    }

    /**
     * 获取主表单信息
     *
     * @param instanceId 流程实例id
     * @return 主表单数据
     */
    @Override
    public Map<String, Object> getMainFormInfo(String instanceId) {
        HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        if (historicInstance == null) {
            throw new AException("未知流程");
        }
        Map<String, Object> result = new HashMap<>();

        // 当前流程的流程变量
        List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        // 业务key
        String businessKey = historicInstance.getBusinessKey();
        // 设置流程变量,根据businessKey回显主表单,因为前端赋值是这样的:
        // variables[`${form.value.businessKey}formData`] = JSON.parse(JSON.stringify(formData));
        // variables[`${form.value.businessKey}formJson`] = form.value.formJson;
        historicVariables.stream()
                .filter(t -> t.getVariableName().equals(String.format("%s_formJson", businessKey)))
                .findAny()
                .ifPresent(t -> result.put("formJson", t.getValue()));

        historicVariables.stream()
                .filter(t -> t.getVariableName().equals(String.format("%s_formData", businessKey)))
                .findAny()
                .ifPresent(t -> result.put("formData", t.getValue()));

        return result;
    }

    /**
     * 获取获选人 或 候选组
     *
     * @param taskId 任务id
     * @return 获选人 或 候选组信息
     */
    public IdentityVo getCandidateInfo(String taskId) {
        IdentityVo vo = new IdentityVo();
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

        // 审批人
        String userId = identityLinks.stream()
                .filter(t -> t.getType().equals(IdentityLinkType.ASSIGNEE))
                .map(HistoricIdentityLink::getUserId)
                .filter(StringUtils::isNotEmpty).findAny().orElse(null);
        if (StringUtils.isNotEmpty(userId)) {
            SysUserEntity user = userService.getById(userId);
            if (user != null) {
                vo.setUserName(user.getUsername());
            }
        }
        return vo;
    }


    /**
     * 获取高亮线
     *
     * @param bpmnModel      bpmn模型
     * @param finishedList   已审批记录
     * @param unfinishedList 待审批记录
     * @return 结果
     */
    private List<HighlightNodeInfoVo> getHighLightedFlows(BpmnModel bpmnModel,
                                                          List<HistoricActivityInstance> finishedList,
                                                          List<HistoricActivityInstance> unfinishedList) {
        // 高亮流程已发生流转的线id集合
        List<HighlightNodeInfoVo> highLightedFlowIds = new ArrayList<>();
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (int i = 0; i < finishedList.size(); i++) {
            HistoricActivityInstance hai = finishedList.get(i);
            // 获得当前活动对应的节点信息及outgoingFlows信息
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
            // 如果当前节点不是最后一个节点，则取出下一个节点，取出的同时判断是否满足连线条件
            if (i == finishedList.size() - 1) continue;
            FlowNode targetFlowNode = (FlowNode) bpmnModel.getFlowElement(finishedList.get(i + 1).getActivityId());
            // 遍历outgoingFlows并找到匹配线路，保存高亮显示
            for (SequenceFlow sequenceFlow : sequenceFlowList) {
                String ref = sequenceFlow.getTargetRef();
                if (ref.equals(targetFlowNode.getId())) {
                    highLightedFlowIds.add(new HighlightNodeInfoVo() {{
                        setActivityId(sequenceFlow.getId());
                        setStatus(NodeStatus.EXECUTED);
                    }});
                }
            }
        }
        // 高亮待审批的线
        for (int i = 0; i < unfinishedList.size(); i++) {
            // 判断下一个活动的节点和最后一个历史节点的线是否连接
            HistoricActivityInstance hai = unfinishedList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
            HistoricActivityInstance finishedHai = finishedList.get(finishedList.size() - 1);
            incomingFlows.stream()
                    .filter(t -> t.getSourceRef().equals(finishedHai.getActivityId()))
                    .findAny()
                    .ifPresent(sequenceFlow -> {
                        highLightedFlowIds.add(new HighlightNodeInfoVo() {{
                            setActivityId(sequenceFlow.getId());
                            setStatus(NodeStatus.UNFINISHED);
                        }});
                    });
        }
        return highLightedFlowIds;
    }
}
