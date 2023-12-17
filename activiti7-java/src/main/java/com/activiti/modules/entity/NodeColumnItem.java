package com.activiti.modules.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 绑定的具体字段
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class NodeColumnItem implements Serializable {

    /**
     * 表字段名称
     */
    private String columnName;

    /**
     * 行备注
     */
    private String columnComment;
}
