package com.activiti.modules.service;


import com.activiti.modules.entity.dto.workflow.TodoApprovalDto;
import com.activiti.modules.entity.dto.workflow.TodoListDto;
import com.activiti.utils.page.TableDataInfo;

/**
 * 流程启动
 *
 * @author liuguofeng
 * @date 2023/11/04 19:14
 **/
public interface ProcessTodoService {

    /**
     * 查看我代办的流程
     *
     * @param dto 参数
     */
    TableDataInfo queryPage(TodoListDto dto);

    /**
     * 节点审批
     *
     * @param dto 参数
     */
    void approval(TodoApprovalDto dto);
}
