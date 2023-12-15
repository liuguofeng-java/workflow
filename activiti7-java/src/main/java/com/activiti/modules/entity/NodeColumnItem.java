package com.activiti.modules.entity;

import lombok.Data;

/**
 * 绑定的具体字段
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class NodeColumnItem {

    /**
     * 行名称
     */
    private String columnName;

    /**
     * 行备注
     */
    private String columnComment;
}
