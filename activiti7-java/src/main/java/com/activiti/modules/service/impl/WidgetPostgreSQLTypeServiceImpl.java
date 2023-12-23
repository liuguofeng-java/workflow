package com.activiti.modules.service.impl;

import com.activiti.modules.service.WidgetDataTypeService;
import com.activiti.utils.constant.WidgetTypes;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端组件和数据库类型对应关系(postgreSQl实现)
 *
 * @author liuguofeng
 * @date 2023/12/19 10:31
 **/
@Service
public class WidgetPostgreSQLTypeServiceImpl implements WidgetDataTypeService {

    /**
     * postgreSQl组件对应数据库的类型
     *
     * @return 结果
     */
    @Override
    public Map<String, String[]> getWidgetDataType() {
        Map<String, String[]> widgetType = new HashMap<>();
        // 输入框
        widgetType.put(WidgetTypes.WIDGET_INPUT, new String[]{"varchar", "text"});
        // 文本输入框
        widgetType.put(WidgetTypes.WIDGET_TEXTAREA, new String[]{"varchar", "text"});
        // 数字输入框
        widgetType.put(WidgetTypes.WIDGET_NUMBER, new String[]{"int2", "int4", "int8", "float4", "float8", "decimal", "numeric"});
        // 单选框
        widgetType.put(WidgetTypes.WIDGET_RADIO, new String[]{"int2", "int4", "int8"});
        // 多选框
        widgetType.put(WidgetTypes.WIDGET_CHECKBOX, new String[]{"json"});
        // 选择框
        widgetType.put(WidgetTypes.WIDGET_SELECT, new String[]{"int2", "int4", "int8"});
        // 时间
        widgetType.put(WidgetTypes.WIDGET_TIME, new String[]{"time"});
        // 时间范围
        widgetType.put(WidgetTypes.WIDGET_TIME_RANGE, new String[]{"json"});
        // 日期
        widgetType.put(WidgetTypes.WIDGET_DATE, new String[]{"date"});
        // 日期范围
        widgetType.put(WidgetTypes.WIDGET_DATE_RANGE, new String[]{"json"});
        // 开关
        widgetType.put(WidgetTypes.WIDGET_SWITCH, new String[]{"bool"});
        // 评价
        widgetType.put(WidgetTypes.WIDGET_RATE, new String[]{"int2", "int4", "int8"});
        // 颜色
        widgetType.put(WidgetTypes.WIDGET_COLOR, new String[]{"varchar", "text"});
        // 滑块
        widgetType.put(WidgetTypes.WIDGET_SLIDER, new String[]{"int2", "int4", "int8"});
        // 图片上传
        widgetType.put(WidgetTypes.WIDGET_PICTURE_UPLOAD, new String[]{"json"});
        // 文件上传
        widgetType.put(WidgetTypes.WIDGET_FILE_UPLOAD, new String[]{"json"});
        // 富文本
        widgetType.put(WidgetTypes.WIDGET_RICH_EDITOR, new String[]{"int2", "int4", "int8"});
        // 级联选择
        widgetType.put(WidgetTypes.WIDGET_CASCADER, new String[]{"json"});
        return widgetType;
    }

    /**
     * postgreSQl默认组件对应数据库的类型
     * Map.Entry key:类型名称，value:数据库字段长度
     *
     * @return 结果
     */
    @Override
    public Map<String, Map.Entry<String, Integer>> getDefaultWidgetDataType() {
        Map<String, Map.Entry<String, Integer>> result = new HashMap<>();
        // 输入框
        result.put(WidgetTypes.WIDGET_INPUT, new AbstractMap.SimpleEntry<>("varchar", 40));
        // 文本输入框
        result.put(WidgetTypes.WIDGET_TEXTAREA, new AbstractMap.SimpleEntry<>("text", -1));
        // 数字输入框
        result.put(WidgetTypes.WIDGET_NUMBER, new AbstractMap.SimpleEntry<>("float8", -1));
        // 单选框
        result.put(WidgetTypes.WIDGET_RADIO, new AbstractMap.SimpleEntry<>("int2", -1));
        // 多选框
        result.put(WidgetTypes.WIDGET_CHECKBOX, new AbstractMap.SimpleEntry<>("json", -1));
        // 选择框
        result.put(WidgetTypes.WIDGET_SELECT, new AbstractMap.SimpleEntry<>("int2", -1));
        // 时间
        result.put(WidgetTypes.WIDGET_TIME, new AbstractMap.SimpleEntry<>("time", -1));
        // 时间范围
        result.put(WidgetTypes.WIDGET_TIME_RANGE, new AbstractMap.SimpleEntry<>("json", -1));
        // 日期
        result.put(WidgetTypes.WIDGET_DATE, new AbstractMap.SimpleEntry<>("date", -1));
        // 日期范围
        result.put(WidgetTypes.WIDGET_DATE_RANGE, new AbstractMap.SimpleEntry<>("json", -1));
        // 开关
        result.put(WidgetTypes.WIDGET_SWITCH, new AbstractMap.SimpleEntry<>("bool", -1));
        // 评价
        result.put(WidgetTypes.WIDGET_RATE, new AbstractMap.SimpleEntry<>("int2", -1));
        // 颜色
        result.put(WidgetTypes.WIDGET_COLOR, new AbstractMap.SimpleEntry<>("varchar", 20));
        // 滑块
        result.put(WidgetTypes.WIDGET_SLIDER, new AbstractMap.SimpleEntry<>("int2", -1));
        // 图片上传
        result.put(WidgetTypes.WIDGET_PICTURE_UPLOAD, new AbstractMap.SimpleEntry<>("json", -1));
        // 文件上传
        result.put(WidgetTypes.WIDGET_FILE_UPLOAD, new AbstractMap.SimpleEntry<>("json", -1));
        // 富文本
        result.put(WidgetTypes.WIDGET_RICH_EDITOR, new AbstractMap.SimpleEntry<>("text", -1));
        // 级联选择
        result.put(WidgetTypes.WIDGET_CASCADER, new AbstractMap.SimpleEntry<>("json", -1));
        return result;
    }
}
