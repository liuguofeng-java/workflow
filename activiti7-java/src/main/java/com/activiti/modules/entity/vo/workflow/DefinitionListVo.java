package com.activiti.modules.entity.vo.workflow;

import lombok.Data;

import java.util.Map;

/**
 * 流程管理列表
 *
 * @author liuguofeng
 * @date 2023/10/21 12:38
 **/
@Data
public class DefinitionListVo {
    /**
     * 流程id
     */
    private String id;

    /**
     * 部署id
     */
    private String deploymentId;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程key
     */
    private String key;

    /**
     * 版本
     */
    private int version;

    /**
     * 主表单表单详情
     */
    private Map<String, Object> formJson;



}
