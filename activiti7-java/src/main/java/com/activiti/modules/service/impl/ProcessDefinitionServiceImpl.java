package com.activiti.modules.service.impl;

import com.activiti.modules.entity.dto.ProcessDefinitionDto;
import com.activiti.modules.entity.vo.ProcessDefinitionVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义实现
 *
 * @author liuguofeng
 * @date 2023/10/21 11:31
 **/
@Service("processDefinitionService")
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;


    /**
     * 流程管理列表
     *
     * @param dto 参数
     * @return 列表
     */
    @Override
    public TableDataInfo queryPage(ProcessDefinitionDto dto) {
        PageDomain params = PageUtils.getPageParams();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionId()
                .orderByProcessDefinitionVersion()
                .desc();
        query.processDefinitionNameLike("%" + dto.getDefinitionName() + "%");
        query.processDefinitionKeyLike("%" + dto.getDefinitionKey() + "%");
        List<ProcessDefinition> list =
                query.listPage(params.getPageNo() - 1, params.getPageSize());
        List<ProcessDefinitionVo> resultList = new ArrayList<>();
        for (ProcessDefinition item : list) {
            ProcessDefinitionVo vo = new ProcessDefinitionVo();
            BeanUtils.copyProperties(item, vo);
            resultList.add(vo);
        }
        return PageUtils.getDataTable(resultList, query.count());
    }

    /**
     * 获取流程定义xml
     *
     * @param deploymentId 部署id
     * @return 流程xml字符串
     */
    @Override
    public String getDefinitionXml(String deploymentId) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, "index.bpmn");
        byte[] bytes = new byte[inputStream.available()];
        while (inputStream.read(bytes) != -1);
        inputStream.close();
        return new String(bytes);
    }

    /**
     * 部署流程xml
     *
     * @param xmlStr xml字符串
     */
    @Override
    public void deploymentXmlByStr(String xmlStr) {
        repositoryService.createDeployment()
                .addString("index.bpmn", xmlStr)
                .deploy();
    }

    /**
     * 删除流程
     *
     * @param deploymentId 部署id
     */
    @Override
    public void delete(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
