package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

/**
 * 审批节点
 *
 * @author liuguofeng
 * @date 2023/11/05 09:00
 **/
@Data
public class TodoApprovalDto {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 处理意见
     */
    private String comment;
}
