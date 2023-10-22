package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysDeptDao;
import com.activiti.modules.entity.SysDeptEntity;
import com.activiti.modules.service.SysDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

    /**
     * 部门分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    @Override
    public List<SysDeptEntity> queryPage(SysDeptEntity dto) {
        return baseMapper.queryPage(dto);
    }
}