package com.activiti.modules.service;

import com.activiti.modules.entity.SysListenerEntity;
import com.activiti.modules.entity.dto.SysListenerListDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 执行监听器
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-12-06 22:18:27
 */
public interface SysListenerService extends IService<SysListenerEntity> {

    /**
     * 执行监听器分页列表
     *
     * @param dto 分页参数
     * @return 列表
     */
    List<SysListenerEntity> queryPage(SysListenerListDto dto);
}

