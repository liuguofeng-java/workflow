package com.activiti.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取Properties工具类
 *
 * @author liuguofeng
 * @date 2023/12/24 11:40
 **/
public class PropertiesUtils {
    /**
     * 读取Properties
     *
     * @param path 路径
     * @return 结果
     */
    public static Properties getProperties(String path) {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = PropertiesUtils.class.getResourceAsStream(path);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 组件对应数据库的类型
     *
     * @param fileName 文件名称
     * @return 结果
     */
    public static Map<String, String[]> getWidgetDataType(String fileName) {
        String path = String.format("/widgetDataType/%s.properties", fileName);
        Properties properties = getProperties(path);
        Map<String, String[]> widgetType = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("widget.")) {
                String[] types = properties.getProperty(key).split(",");
                widgetType.put(key.replace("widget.", ""), types);
            }
        }
        return widgetType;
    }

    /**
     * 默认组件对应数据库的类型 key=数据库类型,value=数据库字段长度,-1长度为0
     *
     * @param fileName 文件名称
     * @return 结果
     */
    public static Map<String, Map.Entry<String, Integer>> getDefaultWidgetDataType(String fileName) {
        String path = String.format("/widgetDataType/%s.properties", fileName);
        Properties properties = getProperties(path);
        Map<String, Map.Entry<String, Integer>> defaultTypes = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("default.")) {
                String value = properties.getProperty(key);
                String[] parts = value.split(":");
                defaultTypes.put(key.replace("default.", ""),
                        new AbstractMap.SimpleEntry<>(parts[0], Integer.parseInt(parts[1])));
            }
        }
        return defaultTypes;
    }
}
