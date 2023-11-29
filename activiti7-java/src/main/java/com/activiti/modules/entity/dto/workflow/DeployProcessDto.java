package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 部署流程
 *
 * @author liuguofeng
 * @date 2023/11/29 17:10
 **/
@Data
public class DeployProcessDto {
    /**
     * bpmn xml
     */
    private String xml;

    /**
     * 表单数据
     */
    private List<FormJsons> formJsonList;


}
