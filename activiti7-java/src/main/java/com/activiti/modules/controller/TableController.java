package com.activiti.modules.controller;

import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.TableInfo;
import com.activiti.modules.service.TableService;
import com.activiti.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author liuguofeng
 * @date 2023/12/13 15:23
 **/
@RequestMapping("table")
@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * 获取组件类型
     *
     * @return 结果
     */
    @GetMapping("getWidgetDataType")
    public R<Map<String, Object>> getWidgetDataType() {
        Map<String, Object> map = tableService.getWidgetDataType();
        return R.ok(map);
    }

    /**
     * 表名称
     *
     * @param tableName 数据库表信息
     * @return 结果
     */
    @GetMapping("list")
    public R<List<TableInfo>> list(String tableName) {
        List<TableInfo> list = tableService.tableList(tableName);
        return R.ok(list);
    }

    /**
     * 数据库表结构
     *
     * @param tableName 表名称或表备注
     * @return 表结构信息
     */
    @GetMapping("tableColumns")
    public R<List<TableColumns>> tableColumns(@RequestParam String tableName,
                                              @RequestParam(required = false) String columnKey) {
        List<TableColumns> columns = tableService.tableColumns(tableName, columnKey);
        return R.ok(columns);
    }


}
