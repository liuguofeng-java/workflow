package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysFormDao;
import com.activiti.modules.entity.SysFormEntity;
import com.activiti.modules.service.SysFormService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;


@Service("sysFormService")
public class SysFormServiceImpl extends ServiceImpl<SysFormDao, SysFormEntity> implements SysFormService {


    /**
     * 流程表单分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    @Override
    public List<SysFormEntity> queryPage(SysFormEntity dto) {
        return this.baseMapper.queryPage(dto);
    }
}
