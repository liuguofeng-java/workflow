package com.activiti.modules.dao;

import com.activiti.modules.entity.SysDeptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门表
 * 
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-22 11:27:47
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 部门分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysDeptEntity> queryPage(SysDeptEntity dto);
}
