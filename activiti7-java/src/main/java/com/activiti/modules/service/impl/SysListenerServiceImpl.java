package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysListenerDao;
import com.activiti.modules.entity.SysListenerEntity;
import com.activiti.modules.entity.dto.SysListenerListDto;
import com.activiti.modules.service.SysListenerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;


@Service("sysListenerService")
public class SysListenerServiceImpl extends ServiceImpl<SysListenerDao, SysListenerEntity> implements SysListenerService {

    /**
     * 执行监听器分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    @Override
    public List<SysListenerEntity> queryPage(SysListenerListDto dto) {
        return baseMapper.queryPage(dto);
    }
}