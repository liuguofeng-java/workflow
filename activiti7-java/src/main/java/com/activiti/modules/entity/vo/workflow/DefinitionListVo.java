package com.activiti.modules.entity.vo.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
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

    /**
     * 是否挂起状态
     */
    private boolean isSuspended;

    /**
     * 部署时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deploymentTime;
}
