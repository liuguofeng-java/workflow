package com.activiti.modules.service;

import com.activiti.modules.entity.SysDeptEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 部门表
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-22 11:27:47
 */
public interface SysDeptService extends IService<SysDeptEntity> {

    /**
     * 部门分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysDeptEntity> queryPage(SysDeptEntity dto);
}

