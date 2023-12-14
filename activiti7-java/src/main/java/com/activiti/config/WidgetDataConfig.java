package com.activiti.config;

import com.activiti.utils.enums.DbType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author liuguofeng
 * @date 2023/12/14 14:53
 **/
@Component
public class WidgetDataConfig {

    @Value("${system.database}")
    private DbType database;

    /**
     * 组件对应数据库的类型
     */
    public static final Map<String, String[]> WIDGET_TYPE = new HashMap<>();

    /**
     * 组件默认数据库类型
     */
    public static final Map<String, String> WIDGET_DEFAULT_TYPE = new HashMap<>();

    /**
     * 初始化组件类型
     */
    @Bean
    public void widgetTypeInit() {
        // 输入框
        WIDGET_TYPE.put("input", new String[]{
                "varchar", "tinytext", "text", "mediumtext", "longtext"
        });
        // 文本输入框
        WIDGET_TYPE.put("textarea", new String[]{
                "varchar", "tinytext", "text", "mediumtext", "longtext"
        });
        // 数字输入框
        WIDGET_TYPE.put("number", new String[]{
                "int", "integer", "bigint", "float", "double", "decimal"
        });
        // 单选框
        WIDGET_TYPE.put("radio", new String[]{
                "tinyint", "integer"
        });
        // 多选框
        WIDGET_TYPE.put("checkbox", new String[]{
                "json"
        });
        // 选择框
        WIDGET_TYPE.put("select", new String[]{
                "tinyint", "integer"
        });
        // 时间
        WIDGET_TYPE.put("time", new String[]{
                "time"
        });
        // 时间范围
        WIDGET_TYPE.put("time-range", new String[]{
                "json"
        });
        // 日期
        WIDGET_TYPE.put("date", new String[]{
                "date"
        });
        // 日期范围
        WIDGET_TYPE.put("date-range", new String[]{
                "json"
        });
        // 开关
        WIDGET_TYPE.put("switch", new String[]{
                "tinyint"
        });
        // 评价
        WIDGET_TYPE.put("rate", new String[]{
                "tinyint", "integer"
        });
        // 颜色
        WIDGET_TYPE.put("color", new String[]{
                "varchar", "tinytext", "text", "mediumtext", "longtext"
        });
        // 滑块
        WIDGET_TYPE.put("slider", new String[]{
                "tinyint", "integer"
        });
        // 图片上传
        WIDGET_TYPE.put("picture-upload", new String[]{
                "json"
        });
        // 文件上传
        WIDGET_TYPE.put("file-upload", new String[]{
                "json"
        });
        // 富文本
        WIDGET_TYPE.put("rich-editor", new String[]{
                "varchar", "tinytext", "text", "mediumtext", "longtext"
        });
    }

    /**
     * 初始化组件类型
     */
    @Bean
    public void widgetDefaultTypeInit() {
        if (database == DbType.MY_SQL) {
            // 输入框
            WIDGET_DEFAULT_TYPE.put("input", "varchar");
            // 文本输入框
            WIDGET_DEFAULT_TYPE.put("textarea", "text");
            // 数字输入框
            WIDGET_DEFAULT_TYPE.put("number", "double");
            // 单选框
            WIDGET_DEFAULT_TYPE.put("radio", "tinyint");
            // 多选框
            WIDGET_DEFAULT_TYPE.put("checkbox", "json");
            // 选择框
            WIDGET_DEFAULT_TYPE.put("select", "tinyint");
            // 时间
            WIDGET_DEFAULT_TYPE.put("time", "time");
            // 时间范围
            WIDGET_DEFAULT_TYPE.put("time-range", "json");
            // 日期
            WIDGET_DEFAULT_TYPE.put("date", "date");
            // 日期范围
            WIDGET_DEFAULT_TYPE.put("date-range", "json");
            // 开关
            WIDGET_DEFAULT_TYPE.put("switch", "tinyint");
            // 评价
            WIDGET_DEFAULT_TYPE.put("rate", "tinyint");
            // 颜色
            WIDGET_DEFAULT_TYPE.put("color", "varchar");
            // 滑块
            WIDGET_DEFAULT_TYPE.put("slider", "tinyint");
            // 图片上传
            WIDGET_DEFAULT_TYPE.put("picture-upload", "json");
            // 文件上传
            WIDGET_DEFAULT_TYPE.put("file-upload", "json");
            // 富文本
            WIDGET_DEFAULT_TYPE.put("rich-editor", "text");
        } else if (database == DbType.POSTGRE_SQL) {

        }


    }
}
