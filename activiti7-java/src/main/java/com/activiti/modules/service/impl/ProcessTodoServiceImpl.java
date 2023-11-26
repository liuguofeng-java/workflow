package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysFormEntity;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.TodoApprovalDto;
import com.activiti.modules.entity.dto.workflow.TodoListDto;
import com.activiti.modules.entity.vo.workflow.TodoListVo;
import com.activiti.modules.service.ProcessTodoService;
import com.activiti.modules.service.SysFormService;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import de.odysseus.el.tree.TreeBuilderException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 代办任务
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

    @Autowired
    private SysFormService formService;

    /**
     * 查看我代办的流程
     *
     * @param dto 参数
     */
    @Override
    public TableDataInfo queryPage(TodoListDto dto) {
        List<String> usersGroups = new ArrayList<>();
        usersGroups.add(dto.getDeptId());
        PageDomain params = PageUtils.getPageParams();
        TaskQuery query = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(dto.getUserId(), usersGroups)
                .processDefinitionNameLike("%" + dto.getDefinitionName() + "%")
                .processDefinitionKeyLike("%" + dto.getDefinitionKey() + "%")
                .orderByTaskCreateTime()
                .desc();
        List<Task> list = query
                .listPage(params.getPageNo() - 1, params.getPageSize());

        List<TodoListVo> resultList = new ArrayList<>();
        for (Task task : list) {
            TodoListVo vo = new TodoListVo();
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
            SysUserEntity user = userService.getById((instance.getStartUserId()));
            vo.setStartUserId(user.getUserId());
            vo.setStartUserName(user.getUsername());
            resultList.add(vo);
        }
        return PageUtils.getDataTable(resultList, query.count());
    }

    /**
     * 获取节点表单
     *
     * @param taskId 任务id
     * @return 表单数据
     */
    @Override
    public String getNodeForm(String taskId) {
        // 获取任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) throw new AException("未知任务！");

        // 获取当前节点 formKey
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        // 只有用户节点才有表单
        if (!(flowElement instanceof UserTask)) throw new AException("只能获取用户节点表单");
        String formKey = ((UserTask) flowElement).getFormKey();

        // 当前节点没有设置表单
        if (StringUtils.isEmpty(formKey)) return "";
        // 获取表单数据
        SysFormEntity form = formService.getOne(new LambdaQueryWrapper<SysFormEntity>()
                .eq(SysFormEntity::getFormId, formKey));
        // 没有数据可能被删除了
        if (form == null) {
            throw new AException("没有找到动态表单,数据可能已被删除!");
        }
        return form.getFormData();
    }

    /**
     * 节点审批
     *
     * @param dto 参数
     */
    @Override
    public void approval(TodoApprovalDto dto) {
        try {
            List<String> usersGroups = new ArrayList<>();
            usersGroups.add(dto.getDeptId());
            TaskQuery query = taskService.createTaskQuery()
                    .active()
                    .includeProcessVariables()
                    .processInstanceId(dto.getProcessInstanceId())
                    .taskCandidateOrAssigned(dto.getUserId(), usersGroups)
                    .orderByTaskCreateTime()
                    .desc();
            List<Task> list = query.list();
            if (list.size() == 0) throw new AException("未找到审批节点!");
            Task task = list.get(0);

            // 如果没有代理人就拾取任务进行办理
            if (StringUtils.isEmpty(task.getAssignee())) {
                taskService.claim(task.getId(), dto.getUserId());
            }

            taskService.addComment(task.getId(), task.getProcessInstanceId(), dto.getComment());
            taskService.complete(task.getId(), dto.getVariables());
        } catch (ActivitiException ex) {
            throw new AException("Activiti流程异常:" + ex.getMessage());
        } catch (TreeBuilderException ex) {
            throw new AException("流程条件错误" + ex.getMessage());
        } catch (Exception ex) {
            throw new AException("流程提交未知异常!");
        }
    }
}
