package com.activiti.modules.entity.vo.workflow;

import lombok.Data;

/**
 * 高亮节点信息
 * @author liuguofeng
 * @date 2023/11/06 13:53
 **/
@Data
public class HighlightNodeInfoVo {
    /**
     * 节点id
     */
    private String activityId;

    /**
     * 节点名称
     */
    private String activityType;

    /**
     * 状态 1:已完成节点,2:活动的未处理的节点(下一个节点)
     */
    private Integer status;
}
