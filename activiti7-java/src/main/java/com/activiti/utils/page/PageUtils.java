package com.activiti.utils.page;

import com.activiti.utils.constant.HttpStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtils extends PageHelper {
    /**
     * 获取分页参数
     */
    public static PageDomain getPageParams() {
        return TableSupport.buildPageRequest();
    }

    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNo = pageDomain.getPageNo();
        Integer pageSize = pageDomain.getPageSize();
        PageHelper.startPage(pageNo, pageSize);
    }


    /**
     * 返回列表数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回列表数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static TableDataInfo getDataTable(List<?> list, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }
}
