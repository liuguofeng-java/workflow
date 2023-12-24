package com.activiti.config;

import com.activiti.modules.dao.TableDao;
import com.activiti.modules.dao.TableMySQLDao;
import com.activiti.modules.dao.TablePostgreSQLDao;
import com.activiti.utils.PropertiesUtils;
import com.activiti.utils.enums.DbType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

import static com.activiti.utils.enums.DbType.MY_SQL;
import static com.activiti.utils.enums.DbType.POSTGRE_SQL;


/**
 * 数据库配置
 *
 * @author liuguofeng
 * @date 2023/12/13 11:09
 **/
@Configuration
public class DbConfig {
    @Value("${system.database}")
    private DbType database;

    @Getter
    private Map<String, String[]> widgetDataType;
    @Getter
    private Map<String, Map.Entry<String, Integer>> defaultWidgetDataType;


    // 数据库操作实现
    @Autowired
    private TableMySQLDao tableMySQLDao;
    @Autowired
    private TablePostgreSQLDao tablePostgreSQLDao;

    /**
     * 切换数据库
     */
    @Bean
    @Primary
    public TableDao getTableDao() {
        switch (database) {
            case MY_SQL:
                widgetDataType = PropertiesUtils.getWidgetDataType(MY_SQL.getKey());
                defaultWidgetDataType = PropertiesUtils.getDefaultWidgetDataType(MY_SQL.getKey());
                return tableMySQLDao;
            case POSTGRE_SQL:
                widgetDataType = PropertiesUtils.getWidgetDataType(POSTGRE_SQL.getKey());
                defaultWidgetDataType = PropertiesUtils.getDefaultWidgetDataType(POSTGRE_SQL.getKey());
                return tablePostgreSQLDao;
            default:
                throw new RuntimeException("不支持当前数据库：" + database);
        }
    }

}
