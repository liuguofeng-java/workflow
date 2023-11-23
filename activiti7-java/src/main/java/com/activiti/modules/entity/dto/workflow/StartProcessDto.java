package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import java.util.Map;

/**
 * 启动流程实例参数
 *
 * @author liuguofeng
 * @date 2023/11/23 21:05
 **/
@Data
public class StartProcessDto {
    /**
     * 业务id
     */
    private String businessKey;

    /**
     * 流程实例id
     */
    private String definitionId;

    /**
     * 启动流程时的表单参数,用于流程变量和回显
     */
    private Map<String, Object> variables;
}
