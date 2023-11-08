package com.activiti.modules.service.impl;

import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.vo.workflow.DefinitionListVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.exception.AException;
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
    public TableDataInfo queryPage(DefinitionListDto dto) {
        PageDomain params = PageUtils.getPageParams();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionId()
                .orderByProcessDefinitionKey().desc()
                .orderByProcessDefinitionVersion().desc();
        query.processDefinitionNameLike("%" + dto.getDefinitionName() + "%");
        query.processDefinitionKeyLike("%" + dto.getDefinitionKey() + "%");
        List<ProcessDefinition> list =
                query.listPage(params.getPageNo() - 1, params.getPageSize());
        List<DefinitionListVo> resultList = new ArrayList<>();
        for (ProcessDefinition item : list) {
            DefinitionListVo vo = new DefinitionListVo();
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
    public String getDefinitionXml(String deploymentId) {
        InputStream is = null;
        try {
            is = repositoryService.getResourceAsStream(deploymentId, "index.bpmn");
            byte[] bytes = new byte[is.available()];
            while (is.read(bytes) != -1) ;
            return new String(bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new AException("获取流程图失败");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 部署流程xml
     *
     * @param xmlStr xml字符串
     */
    @Override
    public void deploymentXmlByStr(String xmlStr) {
        repositoryService.createDeployment().disableBpmnValidation()
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
