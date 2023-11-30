package com.activiti.modules.service;


import com.activiti.modules.entity.dto.workflow.TodoApprovalDto;
import com.activiti.modules.entity.dto.workflow.TodoListDto;
import com.activiti.utils.R;
import com.activiti.utils.page.TableDataInfo;

import java.util.Map;

/**
 * 代办任务
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
     * 获取节点表单
     * @param taskId 任务id
     * @return 表单数据
     */
    Map<String, Object> getNodeForm(String taskId);

    /**
     * 节点审批
     *
     * @param dto 参数
     */
    void approval(TodoApprovalDto dto);

}
