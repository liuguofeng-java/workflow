package com.activiti.utils.page;

import lombok.Data;

/**
 * 分页数据
 */
@Data
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNo;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;
}
