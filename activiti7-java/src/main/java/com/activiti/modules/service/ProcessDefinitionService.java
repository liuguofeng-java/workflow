package com.activiti.modules.service;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.ProcessDefinitionListDto;
import com.activiti.utils.page.TableDataInfo;

import java.util.List;

/**
 * 流程管理
 *
 * @author liuguofeng
 * @date 2023/10/21 11:31
 **/
public interface ProcessDefinitionService {

    /**
     * 流程管理列表
     *
     * @param dto 参数
     * @return 列表
     */
    TableDataInfo queryPage(ProcessDefinitionListDto dto);
}
