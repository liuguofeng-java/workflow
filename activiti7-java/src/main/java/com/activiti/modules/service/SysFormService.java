package com.activiti.modules.service;

import com.activiti.modules.entity.SysFormEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * activiti相关->流程表单
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-27 09:44:39
 */
public interface SysFormService extends IService<SysFormEntity> {

    /**
     * 流程表单分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysFormEntity> queryPage(SysFormEntity dto);
}

