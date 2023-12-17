package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;

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
    @NotBlank(message = "bpmn不能为空")
    private String xml;

    /**
     * 表单数据
     */
    private List<FormJsonsDto> formJsonList;

    /**
     * 节点绑定字段信息
     */
    @Valid
    private List<NodeColumnsDto> nodeColumns;

    /**
     * 数据库表信息
     */
    @Valid
    private TableInfoDto tableInfo;


}
