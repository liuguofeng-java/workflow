package com.activiti.modules.service.impl;

import com.activiti.config.WidgetDataConfig;
import com.activiti.modules.dao.TableDao;
import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.TableInfo;
import com.activiti.modules.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库表
 *
 * @author liuguofeng
 * @date 2023/12/13 09:52
 **/
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableDao tableDao;

    /**
     * 获取组件类型
     *
     * @return 结果
     */
    @Override
    public Map<String, Object> getWidgetType() {
        Map<String, Object> map = new HashMap<>();
        map.put("widgetType", WidgetDataConfig.WIDGET_TYPE);
        map.put("widgetDefaultType", WidgetDataConfig.WIDGET_DEFAULT_TYPE);
        return map;
    }

    /**
     * 数据库表信息
     *
     * @param tableName 表名称或表备注
     * @return 表信息
     */
    @Override
    public List<TableInfo> tableList(String tableName) {
        return tableDao.tableList(tableName);
    }

    /**
     * 数据库表结构
     *
     * @param tableName 表名称或表备注
     * @return 表结构信息
     */
    @Override
    public List<TableColumns> tableColumns(String tableName) {
        return tableDao.tableColumns(tableName);
    }
}
