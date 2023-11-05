package com.activiti.modules.service;

import com.activiti.modules.entity.dto.ProcessDefinitionListDto;
import com.activiti.utils.page.TableDataInfo;

import java.io.IOException;

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
    TableDataInfo queryPage(ProcessDefinitionListDto dto);

    /**
     * 获取流程定义xml
     * @param deploymentId 部署id
     * @return 流程xml字符串
     */
    String getDefinitionXml(String deploymentId) throws IOException;

    /**
     * 部署流程xml
     * @param xmlStr xml字符串
     */
    void deploymentXmlByStr(String xmlStr);

    /**
     * 删除流程
     * @param deploymentId 部署id
     */
    void delete(String deploymentId);
}
