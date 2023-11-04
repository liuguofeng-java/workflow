package com.activiti.modules.service.impl;

import com.activiti.modules.entity.dto.ProcessStartDto;
import com.activiti.modules.entity.vo.ProcessStartVo;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.utils.constant.Constant;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 我发起的任务列表
     *
     * @param dto 参数
     * @return 结果
     */
    @Override
    public TableDataInfo queryPage(ProcessStartDto dto) {
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
        List<ProcessStartVo> resultList = new ArrayList<>();
        for (HistoricProcessInstance item : list) {
            // 设置流程实例
            ProcessStartVo vo = new ProcessStartVo();
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
