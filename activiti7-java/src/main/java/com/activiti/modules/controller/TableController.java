package com.activiti.modules.controller;

import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.entity.TableInfo;
import com.activiti.modules.service.TableService;
import com.activiti.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
