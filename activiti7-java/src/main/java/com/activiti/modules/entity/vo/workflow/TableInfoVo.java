package com.activiti.modules.entity.vo.workflow;

import lombok.Data;

import java.util.List;

/**
 * 表信息
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class TableInfoVo {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表备注
     */
    private String tableComment;

    /**
     * 数据库表信息
     */
    private List<TableColumnsVo> columns;
}
