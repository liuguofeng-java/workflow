package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.vo.workflow.HighlightNodeInfoVo;
import com.activiti.modules.entity.vo.workflow.HistoryRecordIdentityVo;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.modules.service.*;
import com.activiti.utils.constant.ActivityType;
import com.activiti.utils.constant.NodeStatus;
import com.activiti.utils.exception.AException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    private TaskService taskService;

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @Autowired
    private SysDeptService deptService;

    @Autowired
    private SysUserService userService;

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
            vo.setActivityId(item.getActivityId());
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
            vo.setStatus(NodeStatus.EXECUTED);

            // 设置流程变量,根据activityId回显动态表单(用户提交的数据),因为前端赋值是这样的:
            // variables[`${activityId}_formJson`] = formJson;
            // variables[`${activityId}_formData`] = JSON.parse(JSON.stringify(formData));
            historicVariables.stream()
                    .filter(t -> t.getVariableName().equals(String.format("%s_formJson", item.getActivityId())))
                    .findAny()
                    .ifPresent(t -> vo.setFormJson(t.getValue()));

            historicVariables.stream()
                    .filter(t -> t.getVariableName().equals(String.format("%s_formData", item.getActivityId())))
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
            // 审批人
            if (StringUtils.isNotEmpty(item.getAssignee())) {
                SysUserEntity user = userService.getById(item.getAssignee());
                if (user != null) {
                    vo.setUserName(user.getUsername());
                }
            }
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
        // 已审批审批节点
        List<HistoricActivityInstance> executedList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime().asc()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        executedList.forEach(item -> {
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
                .processInstanceId(instanceId)
                .unfinished()
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
        nodeInfo.addAll(getFlowsStatus(bpmnModel, executedList, unfinishedList));

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
        // 已解决待验证

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
                        setStatus(NodeStatus.EXECUTED);
                    }});
                }
            });
        }

        // 获取历史节点的互斥网关
        List<HistoricActivityInstance> exclusiveGateways = executedList.stream()
                .filter(t -> t.getActivityType().equals(ActivityType.EXCLUSIVE_GATEWAY))
                .collect(Collectors.toList());
        // 互斥网关去除没有不正确的连线
        for (HistoricActivityInstance exclusiveGateway : exclusiveGateways) {
            // 获取到当前节点
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
                    .getFlowElement(exclusiveGateway.getActivityId(), true);
            // 获取输出的线
            List<SequenceFlow> outFlows = flowNode.getOutgoingFlows();
            for (SequenceFlow outFlow : outFlows) {
                String sourceRef = outFlow.getSourceRef();
                String targetRef = outFlow.getTargetRef();
                // 获取互斥网关下标
                int gatewayIndex = IntStream.range(0, executedList.size())
                        .filter(i -> sourceRef.equals(executedList.get(i).getActivityId()))
                        .findFirst()
                        .orElse(-1);
                if (gatewayIndex == -1 || executedList.size() <= gatewayIndex + 1) continue;
                HistoricActivityInstance next = executedList.get(gatewayIndex + 1);
                // 因为executedList历史记录是按照正序排序的,互斥网关下一个节点只能有一个
                // 如果这条线的targetRef不是查到的activityId,那么这条线应该被删除
                if (next != null && !targetRef.equals(next.getActivityId())) {
                    int targetIndex = IntStream.range(0, resultList.size())
                            .filter(i -> outFlow.getId().equals(resultList.get(i).getActivityId()))
                            .findFirst().orElse(-1);
                    if (targetIndex == -1) continue;
                    resultList.remove(targetIndex);
                }
            }
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
                        setStatus(NodeStatus.UNFINISHED);
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
