package com.activiti.modules.entity.vo.workflow;

import com.activiti.utils.constant.NodeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 流程记录
 *
 * @author liuguofeng
 * @date 2023/11/05 15:02
 **/
@Data
public class HistoryRecordVo {
    /**
     * 任务名称
     */
    private String nodeName;

    /**
     * 节点id
     */
    private String activityId;

    /**
     * 任务开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 审批人用户id
     */
    private String userId;

    /**
     * 审批人用户名称
     */
    private String userName;

    /**
     * 状态 1:已完成节点,2:活动的未处理的节点(下一个节点) 参考{@link NodeStatus}
     */
    private Integer status;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 流程记录身份信息
     */
    private HistoryRecordIdentityVo identity;

    /**
     * 动态表单结构数据
     */
    private Object formJson;

    /**
     * 动态表单数据
     */
    private Object formData;
}
