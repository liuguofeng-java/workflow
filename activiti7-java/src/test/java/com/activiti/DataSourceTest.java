package com.activiti;

import com.activiti.modules.dao.TableDao;
import com.activiti.modules.dao.TableMySQLDao;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.TableInfo;
import com.activiti.modules.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liuguofeng
 * @date 2023/10/16 11:50
 **/
@SpringBootTest
public class DataSourceTest {
    @Autowired
    private TableDao tableDao;

    @Test
    public void demo01(){
        List<TableInfo> tableInfos = tableDao.tableList("");
        System.out.println();
    }

    @Test
    public void demo02(){
        List<TableColumns> tableColumns = tableDao.tableColumns("test");
        System.out.println();
    }
}
