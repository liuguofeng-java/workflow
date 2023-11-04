package com.activiti.modules.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 我发起的任务列表返回
 * @author liuguofeng
 * @date 2023/11/04 14:22
 **/
@Data
public class ProcessStartVo {
    /**
     * 流程定义id
     */
    private String id;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 流程部署id
     */
    private String deploymentId;
    /**
     * 流程部署名称
     */
    private String definitionName;

    /**
     * 流程版本号
     */
    private Integer version;

    /**
     * 流程key
     */
    private String key;

    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
}
