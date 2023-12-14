package com.activiti.modules.service;

import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 数据库表
 *
 * @author liuguofeng
 * @date 2023/12/13 09:51
 **/
public interface TableService {

    /**
     * 获取组件类型
     *
     * @return 结果
     */
    Map<String, Object> getWidgetType();

    /**
     * 数据库表信息
     *
     * @param tableName 表名称或表备注
     * @return 表信息
     */
    List<TableInfo> tableList(String tableName);

    /**
     * 数据库表结构
     *
     * @param tableName 表名称或表备注
     * @return 表结构信息
     */
    List<TableColumns> tableColumns(String tableName);

}
