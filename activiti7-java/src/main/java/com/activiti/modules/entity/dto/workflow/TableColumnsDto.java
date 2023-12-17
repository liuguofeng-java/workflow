package com.activiti.modules.entity.dto.workflow;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 表结构
 *
 * @author liuguofeng
 * @date 2023/12/13 10:23
 **/
@Data
public class TableColumnsDto {

    /**
     * 行名称
     */
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "绑定字段-字段名称不符合规则")
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 行备注
     */
    private String columnComment;
    /**
     * 行健
     */
    private String columnKey;

}
