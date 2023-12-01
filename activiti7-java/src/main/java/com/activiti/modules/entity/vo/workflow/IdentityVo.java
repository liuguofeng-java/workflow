package com.activiti.modules.entity.vo.workflow;

import lombok.Data;

import java.util.List;


/**
 * 流程记录身份信息
 *
 * @author liuguofeng
 * @date 2023/11/16 10:04
 **/
@Data
public class IdentityVo {

    /**
     * 审批人用户名称
     */
    private String userName;

    /**
     * 候选人信息
     */
    private List<String> userNames;

    /**
     * 候选组信息
     */
    private List<String> groupNames;
}
