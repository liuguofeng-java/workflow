package com.activiti.modules.dao;

import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 表结构
 *
 * @author liuguofeng
 * @date 2023/12/13 10:16
 **/
@Mapper
public interface TableDao {

    /**
     * 数据库表信息
     *
     * @param tableName 表名称或表备注
     * @return 表信息
     */
    List<TableInfo> tableList(String tableName);

    /**
     * 数据库表结构
     * @param tableName 表名称或表备注
     * @return 表结构信息
     */
    List<TableColumns> tableColumns(String tableName);
}
