package com.activiti.modules.service.impl;

import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.dto.workflow.StartProcessDto;
import com.activiti.modules.entity.vo.workflow.StartListVo;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.modules.service.SysDeployService;
import com.activiti.utils.constant.ActivityType;
import com.activiti.utils.constant.Constants;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private SysDeployService deployService;

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

        // 根据业务key查询 注意是等于不是模糊查询
        if (StringUtils.isNoneEmpty(dto.getBusinessKey())) {
            query.processInstanceBusinessKey(dto.getBusinessKey());
        }
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
            vo.setBusinessKey(item.getBusinessKey());
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
            Task task = taskService.createTaskQuery()
                    .processInstanceId(item.getId())
                    .singleResult();
            if (task != null) {
                vo.setTaskId(task.getId());
                vo.setTaskName(task.getName());
                vo.setStatus(1);
            } else {
                // 任务处理完成在history获取
                List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(item.getId())
                        .orderByHistoricTaskInstanceEndTime()
                        .desc()
                        .list();
                HistoricTaskInstance historicTask = historicTaskInstances.get(0);
                vo.setTaskId(historicTask.getId());
                vo.setTaskName(historicTask.getName());
                vo.setStatus(2);
            }
            resultList.add(vo);
        }
        return PageUtils.getDataTable(resultList, query.count());
    }

    /**
     * 启动流程
     *
     * @param dto    启动流程参数
     * @param userId 当前用户登录id
     */
    @Transactional
    @Override
    public void startProcess(StartProcessDto dto, String userId) {
        // 获取相关数据
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(dto.getDefinitionId()).singleResult();
        if(definition.isSuspended()) throw new AException("流程已挂起,不能启动!");

        // 设置流程发起人用户Id
        Authentication.setAuthenticatedUserId(userId);
        Map<String, Object> variables = dto.getVariables();
        // 设置发起人用户id
        // 如果节点审批人,设置的是发起人,则审批节点的 assignee="${initiator}"
        variables.put(Constants.PROCESS_INITIATOR, userId);
        ProcessInstance instance = runtimeService.startProcessInstanceById(dto.getDefinitionId(), dto.getBusinessKey(), variables);

        // 获取开始事件
        HistoricActivityInstance activityInstance = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instance.getId()).list()
                .stream().filter(t -> t.getActivityType().equals(ActivityType.START_EVENT))
                .findAny().orElse(null);
        // 保存数据
        assert activityInstance != null;
        deployService.saveData(instance.getId(), definition.getDeploymentId(),
                activityInstance.getActivityId(), variables);
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
}
