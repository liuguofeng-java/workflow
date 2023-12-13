package com.activiti.modules.entity;

import lombok.Data;

/**
 * 表信息
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class TableInfo {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表备注
     */
    private String tableComment;

    /**
     * 创建时间
     */
    private String createTime;

}
