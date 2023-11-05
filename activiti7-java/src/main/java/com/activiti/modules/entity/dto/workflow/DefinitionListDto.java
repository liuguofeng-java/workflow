package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

/**
 * 流程定义列表参数
 *
 * @author liuguofeng
 * @date 2023/10/21 11:38
 **/
@Data
public class DefinitionListDto {
    // 流程定义名称
    private String definitionName;

    // 流程定义key
    private String definitionKey;
}
