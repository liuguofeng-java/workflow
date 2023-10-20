package com.activiti.modules.service;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.UserInfoListDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 系统用户
 */
public interface SysUserService extends IService<SysUserEntity> {


    /**
     * 系统用户分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysUserEntity> queryPage(UserInfoListDto dto);
}
