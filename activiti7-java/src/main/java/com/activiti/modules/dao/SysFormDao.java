package com.activiti.modules.dao;

import com.activiti.modules.entity.SysFormEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * activiti相关->流程表单
 * 
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-27 09:44:39
 */
@Mapper
public interface SysFormDao extends BaseMapper<SysFormEntity> {

    /**
     * 流程表单分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysFormEntity> queryPage(SysFormEntity dto);
}
