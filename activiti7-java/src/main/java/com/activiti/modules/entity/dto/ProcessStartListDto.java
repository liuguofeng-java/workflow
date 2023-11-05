package com.activiti.modules.entity.dto;

import lombok.Data;

/**
 * 我发起任务列表参数
 * @author liuguofeng
 * @date 2023/11/04 14:11
 **/
@Data
public class ProcessStartListDto {
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
