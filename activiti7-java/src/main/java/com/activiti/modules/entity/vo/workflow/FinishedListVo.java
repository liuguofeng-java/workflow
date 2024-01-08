package com.activiti.modules.entity.vo.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 我的代办
 *
 * @author liuguofeng
 * @date 2023/11/04 19:24
 **/
@Data
public class FinishedListVo {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 节点key
     */
    private String taskDefinitionKey;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    private String definitionName;

    /**
     * 流程定义key
     */
    private String definitionKey;

    /**
     * 流程定义版本
     */
    private Integer definitionVersion;

    /**
     * 发起人用户id
     */
    private String startUserId;

    /**
     * 发起人用户名称
     */
    private String startUserName;



}
