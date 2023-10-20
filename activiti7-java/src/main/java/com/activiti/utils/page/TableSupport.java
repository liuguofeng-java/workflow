package com.activiti.utils.page;

import com.activiti.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 表格数据处理
 */
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NO = "pageNo";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";


    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNo(toInt(ServletUtils.getParameter(PAGE_NO), 1));
        pageDomain.setPageSize(toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }

    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = value.toString();
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
