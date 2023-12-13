package com.activiti.config;

import com.activiti.modules.dao.TableDao;
import com.activiti.modules.dao.TableMySQLDao;
import com.activiti.modules.dao.TablePostgreSQLDao;
import com.activiti.utils.enums.DbType;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * 数据库配置
 *
 * @author liuguofeng
 * @date 2023/12/13 11:09
 **/
@Setter
@Configuration
public class DbConfig {
    @Value("${system.database}")
    private DbType database;

    @Autowired
    private TableMySQLDao tableMySQLDao;
    @Autowired
    private TablePostgreSQLDao tablePostgreSQLDao;

    /**
     * 切换数据库
     */
    @Bean
    @Primary
    public TableDao getGeneratorDao() {
        switch (database) {
            case MY_SQL:
                return tableMySQLDao;
            case POSTGRE_SQL:
                return tablePostgreSQLDao;
            default:
                throw new RuntimeException("不支持当前数据库：" + database);
        }
    }

}
