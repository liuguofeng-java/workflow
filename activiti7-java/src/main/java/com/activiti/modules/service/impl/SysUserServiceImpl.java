package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysUserDao;
import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

}
