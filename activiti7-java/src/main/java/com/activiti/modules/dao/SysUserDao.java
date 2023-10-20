package com.activiti.modules.dao;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.UserInfoListDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuguofeng
 * @date 2023/10/16 11:45
 **/
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {


    /**
     * 系统用户分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysUserEntity> queryPage(UserInfoListDto dto);
}
