package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "绑定表名称不符合规则")
    private String tableName;

    /**
     * 表备注
     */
    private String tableComment;

    /**
     * 数据库表信息
     */
    @Valid
    private List<TableColumnsDto> columns;
}
