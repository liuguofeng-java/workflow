package com.activiti.modules.service;

import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.dto.workflow.DeployProcessDto;
import com.activiti.utils.page.TableDataInfo;

import java.io.IOException;
import java.util.Map;

/**
 * 流程定义
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
    TableDataInfo queryPage(DefinitionListDto dto);

    /**
     * 获取流程定义xml
     * @param deploymentId 部署id
     * @return 流程xml字符串
     */
    String getDefinitionXml(String deploymentId);

    /**
     * 获取流程定义详情
     *
     * @param deploymentId 部署id
     * @return 流程xml字符串和流程表单
     */
    Map<String,Object> getDefinitionInfo(String deploymentId);


    /**
     * 部署流程
     * @param dto 参数
     */
    void deployProcess(DeployProcessDto dto);

    /**
     * 删除流程
     * @param deploymentId 部署id
     */
    void delete(String deploymentId);

}
