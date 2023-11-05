package com.activiti.modules.service;


import com.activiti.modules.entity.dto.ProcessTodoApprovalDto;
import com.activiti.modules.entity.dto.ProcessTodoListDto;
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
    TableDataInfo queryPage(ProcessTodoListDto dto);

    /**
     * 节点审批
     *
     * @param dto 参数
     */
    void approval(ProcessTodoApprovalDto dto);
}
