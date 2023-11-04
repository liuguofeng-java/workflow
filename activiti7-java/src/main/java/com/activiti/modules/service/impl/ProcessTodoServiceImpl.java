package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.ProcessTodoDto;
import com.activiti.modules.entity.vo.ProcessTodoVo;
import com.activiti.modules.service.ProcessTodoService;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程启动
 *
 * @author liuguofeng
 * @date 2023/11/04 12:09
 **/
@Service("processTodoService")
public class ProcessTodoServiceImpl implements ProcessTodoService {


    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SysUserService userService;

    /**
     * 查看我代办的流程
     *
     * @param dto 参数
     */
    @Override
    public TableDataInfo queryPage(ProcessTodoDto dto) {
        PageDomain params = PageUtils.getPageParams();

        TaskQuery query = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(dto.getUserId())
                .orderByTaskCreateTime()
                .desc();
        List<Task> list = query
                .listPage(params.getPageNo() - 1, params.getPageSize());

        List<ProcessTodoVo> resultList = new ArrayList<>();
        for (Task task : list) {
            ProcessTodoVo vo = new ProcessTodoVo();
            // 当前流程
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setCreateTime(task.getCreateTime());
            vo.setProcessDefinitionId(task.getProcessDefinitionId());

            // 流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            vo.setDefinitionName(definition.getName());
            vo.setDefinitionKey(definition.getKey());
            vo.setDefinitionVersion(definition.getVersion());

            // 流程发起人
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            SysUserEntity startUser = userService.getById((instance.getStartUserId()));
            vo.setStartUserId(startUser.getUserId());
            vo.setStartUserName(startUser.getUsername());
            resultList.add(vo);
        }
        return PageUtils.getDataTable(resultList, query.count());
    }
}
