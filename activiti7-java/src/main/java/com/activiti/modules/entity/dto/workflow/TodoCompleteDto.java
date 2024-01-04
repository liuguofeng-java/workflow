package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import java.util.Map;

/**
 * 审批节点
 *
 * @author liuguofeng
 * @date 2023/11/05 09:00
 **/
@Data
public class TodoCompleteDto {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id(组id)
     */
    private String deptId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;


}
