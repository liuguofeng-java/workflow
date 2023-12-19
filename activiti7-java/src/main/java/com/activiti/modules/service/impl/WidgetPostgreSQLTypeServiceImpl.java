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

        return result;
    }
}
