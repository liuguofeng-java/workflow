package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysDeployDao;
import com.activiti.modules.entity.SysDeployEntity;
import com.activiti.modules.service.SysDeployService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysDeployService")
public class SysDeployServiceImpl extends ServiceImpl<SysDeployDao, SysDeployEntity> implements SysDeployService {


}