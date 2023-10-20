package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysUserDao;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.UserInfoListDto;
import com.activiti.modules.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    /**
     * 系统用户分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    @Override
    public List<SysUserEntity> queryPage(UserInfoListDto dto) {
        return this.baseMapper.queryPage(dto);
    }
}
