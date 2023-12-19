package com.activiti.modules.service.impl;

import com.activiti.modules.service.WidgetDataTypeService;
import com.activiti.utils.constant.WidgetTypes;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端组件和数据库类型对应关系(mysql实现)
 *
 * @author liuguofeng
 * @date 2023/12/19 10:31
 **/
@Service
public class WidgetMySQLTypeServiceImpl implements WidgetDataTypeService {

    /**
     * mysql组件对应数据库的类型
     *
     * @return key:组件名称, value: 对应数据库类型
     */
    @Override
    public Map<String, String[]> getWidgetDataType() {
        Map<String, String[]> widgetType = new HashMap<>();
        // 输入框
        widgetType.put(WidgetTypes.WIDGET_INPUT, new String[]{"varchar", "tinytext", "text", "mediumtext", "longtext"});
        // 文本输入框
        widgetType.put(WidgetTypes.WIDGET_TEXTAREA, new String[]{"varchar", "tinytext", "text", "mediumtext", "longtext"});
        // 数字输入框
        widgetType.put(WidgetTypes.WIDGET_NUMBER, new String[]{"int", "integer", "bigint", "float", "double", "decimal"});
        // 单选框
        widgetType.put(WidgetTypes.WIDGET_RADIO, new String[]{"tinyint", "integer"});
        // 多选框
        widgetType.put(WidgetTypes.WIDGET_CHECKBOX, new String[]{"json"});
        // 选择框
        widgetType.put(WidgetTypes.WIDGET_SELECT, new String[]{"tinyint", "integer"});
        // 时间
        widgetType.put(WidgetTypes.WIDGET_TIME, new String[]{"time"});
        // 时间范围
        widgetType.put(WidgetTypes.WIDGET_TIME_RANGE, new String[]{"json"});
        // 日期
        widgetType.put(WidgetTypes.WIDGET_DATE, new String[]{"date"});
        // 日期范围
        widgetType.put(WidgetTypes.WIDGET_DATE_RANGE, new String[]{"json"});
        // 开关
        widgetType.put(WidgetTypes.WIDGET_SWITCH, new String[]{"tinyint"});
        // 评价
        widgetType.put(WidgetTypes.WIDGET_RATE, new String[]{"tinyint", "integer"});
        // 颜色
        widgetType.put(WidgetTypes.WIDGET_COLOR, new String[]{"varchar", "tinytext", "text", "mediumtext", "longtext"});
        // 滑块
        widgetType.put(WidgetTypes.WIDGET_SLIDER, new String[]{"tinyint", "integer"});
        // 图片上传
        widgetType.put(WidgetTypes.WIDGET_PICTURE_UPLOAD, new String[]{"json"});
        // 文件上传
        widgetType.put(WidgetTypes.WIDGET_FILE_UPLOAD, new String[]{"json"});
        // 富文本
        widgetType.put(WidgetTypes.WIDGET_RICH_EDITOR, new String[]{"varchar", "tinytext", "text", "mediumtext", "longtext"});
        return widgetType;
    }

    /**
     * mysql默认组件对应数据库的类型
     * Map.Entry key:类型名称，value:数据库字段长度-1为没有长度单位
     *
     * @return key:组件名称, value: 对应数据库类型和字段长度
     */
    @Override
    public Map<String, Map.Entry<String, Integer>> getDefaultWidgetDataType() {
        Map<String, Map.Entry<String, Integer>> result = new HashMap<>();
        // 输入框
        result.put(WidgetTypes.WIDGET_INPUT, new AbstractMap.SimpleEntry<>("varchar", 40));
        // 文本输入框
        result.put(WidgetTypes.WIDGET_TEXTAREA, new AbstractMap.SimpleEntry<>("text", -1));
        // 数字输入框
        result.put(WidgetTypes.WIDGET_NUMBER, new AbstractMap.SimpleEntry<>("double", 11));
        // 单选框
        result.put(WidgetTypes.WIDGET_RADIO, new AbstractMap.SimpleEntry<>("tinyint", 5));
        // 多选框
        result.put(WidgetTypes.WIDGET_CHECKBOX, new AbstractMap.SimpleEntry<>("json", -1));
        // 选择框
        result.put(WidgetTypes.WIDGET_SELECT, new AbstractMap.SimpleEntry<>("tinyint", 5));
        // 时间
        result.put(WidgetTypes.WIDGET_TIME, new AbstractMap.SimpleEntry<>("time", -1));
        // 时间范围
        result.put(WidgetTypes.WIDGET_TIME_RANGE, new AbstractMap.SimpleEntry<>("json", -1));
        // 日期
        result.put(WidgetTypes.WIDGET_DATE, new AbstractMap.SimpleEntry<>("date", -1));
        // 日期范围
        result.put(WidgetTypes.WIDGET_DATE_RANGE, new AbstractMap.SimpleEntry<>("json", -1));
        // 开关
        result.put(WidgetTypes.WIDGET_SWITCH, new AbstractMap.SimpleEntry<>("tinyint", -1));
        // 评价
        result.put(WidgetTypes.WIDGET_RATE, new AbstractMap.SimpleEntry<>("tinyint", 5));
        // 颜色
        result.put(WidgetTypes.WIDGET_COLOR, new AbstractMap.SimpleEntry<>("varchar", 20));
        // 滑块
        result.put(WidgetTypes.WIDGET_SLIDER, new AbstractMap.SimpleEntry<>("tinyint", 5));
        // 图片上传
        result.put(WidgetTypes.WIDGET_PICTURE_UPLOAD, new AbstractMap.SimpleEntry<>("json", -1));
        // 文件上传
        result.put(WidgetTypes.WIDGET_FILE_UPLOAD, new AbstractMap.SimpleEntry<>("json", -1));
        // 富文本
        result.put(WidgetTypes.WIDGET_RICH_EDITOR, new AbstractMap.SimpleEntry<>("text", -1));
        return result;
    }
}
