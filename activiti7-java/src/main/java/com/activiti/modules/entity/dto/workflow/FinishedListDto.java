package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

/**
 * 已办任务列表参数
 *
 * @author liuguofeng
 * @date 2024/01/08 09:39
 **/
@Data
public class FinishedListDto {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 流程定义名称
     */
    private String definitionName;

    /**
     * 流程定义key
     */
    private String definitionKey;
}
