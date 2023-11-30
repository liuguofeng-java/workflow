package com.activiti.modules.service.impl;

import com.activiti.modules.entity.SysNodeFormEntity;
import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.dto.workflow.DeployProcessDto;
import com.activiti.modules.entity.dto.workflow.FormJsons;
import com.activiti.modules.entity.vo.workflow.DefinitionListVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.modules.service.SysNodeFormService;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    @Autowired
    private SysNodeFormService sysNodeFormService;

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
            // 获取主表单
            SysNodeFormEntity mainForm = sysNodeFormService.getOne(new LambdaQueryWrapper<SysNodeFormEntity>()
                    .eq(SysNodeFormEntity::getDeployId, item.getDeploymentId())
                    .eq(SysNodeFormEntity::getIsMainFrom, 1));
            if (mainForm != null) {
                vo.setFormJson(mainForm.getFormJson());
            }
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
     * 部署流程
     *
     * @param dto 参数
     */
    @Transactional
    @Override
    public void deployProcess(DeployProcessDto dto) {
        // 部署xml
        Deployment deploy = repositoryService.createDeployment().disableBpmnValidation()
                .addString("index.bpmn", dto.getXml())
                .deploy();

        // 保存节点表单
        List<FormJsons> formJsons = dto.getFormJsonList();
        List<SysNodeFormEntity> list = new ArrayList<>();
        Date date = new Date();
        for (FormJsons formJson : formJsons) {
            list.add(new SysNodeFormEntity() {{
                setDeployId(deploy.getId());
                setActivityId(formJson.getActivityId());
                setFormJson(formJson.getFormJson());
                setIsMainFrom(formJson.getIsMainFrom());
                setCreateTime(date);
            }});
        }
        sysNodeFormService.saveBatch(list);
    }

    /**
     * 获取流程定义详情
     *
     * @param deploymentId 部署id
     * @return 流程xml字符串和流程表单
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getDefinitionInfo(String deploymentId) {
        // 获取xml
        Map<String, Object> result = new HashMap<>();
        String xml = getDefinitionXml(deploymentId);
        result.put("xml", xml);
        // 获取节点表单
        List<SysNodeFormEntity> list = sysNodeFormService.list(new LambdaQueryWrapper<SysNodeFormEntity>()
                .select(SysNodeFormEntity::getActivityId, SysNodeFormEntity::getFormJson, SysNodeFormEntity::getIsMainFrom)
                .eq(SysNodeFormEntity::getDeployId, deploymentId));
        result.put("formJsonList", list);
        return result;
    }

    /**
     * 删除流程
     *
     * @param deploymentId 部署id
     */
    @Transactional
    @Override
    public void delete(String deploymentId) {
        sysNodeFormService.remove(new LambdaQueryWrapper<SysNodeFormEntity>()
                .eq(SysNodeFormEntity::getDeployId, deploymentId));
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
