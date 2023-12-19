package com.activiti.modules.service;

import java.util.Map;

/**
 * 前端组件和数据库类型对应关系
 *
 * @author liuguofeng
 * @date 2023/12/19 10:27
 **/
public interface WidgetDataTypeService {
    /**
     * 组件对应数据库的类型
     *
     * @return key:组件名称, value: 对应数据库类型
     */
    Map<String, String[]> getWidgetDataType();

    /**
     * 默认组件对应数据库的类型
     * Map.Entry key:类型名称，value:数据库字段长度-1为没有长度单位
     *
     * @return key:组件名称, value: 对应数据库类型和字段长度
     */
    Map<String, Map.Entry<String, Integer>> getDefaultWidgetDataType();

}
