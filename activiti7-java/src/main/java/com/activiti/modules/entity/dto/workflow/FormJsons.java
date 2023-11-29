package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import java.util.Map;

/**
 * 动态表单数据结构
 * @author liuguofeng
 * @date 2023/11/29 17:25
 **/
@Data
public class FormJsons {

    /**
     * 流程定义节点唯一标识
     */
    private String activityId;

    /**
     * 表单结构
     */
    private Map<String, Object> formJson;
}
