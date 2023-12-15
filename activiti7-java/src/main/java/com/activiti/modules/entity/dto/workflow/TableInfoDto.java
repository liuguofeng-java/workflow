package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import java.util.List;

/**
 * 表信息
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class TableInfoDto {

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
    private List<TableColumnsDto> columns;
}
