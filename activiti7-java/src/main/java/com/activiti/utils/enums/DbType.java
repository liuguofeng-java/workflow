package com.activiti.utils.enums;

import lombok.Getter;

/**
 * 数据库类型
 *
 * @author liuguofeng
 * @date 2023/12/13 11:12
 **/
@Getter
public enum DbType {
    /**
     * MySql数据库
     */
    MY_SQL("MySQL"),
    /**
     * PostgreSql数据库
     */
    POSTGRE_SQL("PostgreSQl");

    private final String key;
    DbType(String key) {
        this.key = key;
    }
}

