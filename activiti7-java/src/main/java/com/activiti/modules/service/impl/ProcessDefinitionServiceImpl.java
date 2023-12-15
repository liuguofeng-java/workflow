package com.activiti.modules.service.impl;

import com.activiti.modules.dao.TableDao;
import com.activiti.modules.entity.SysDeployEntity;
import com.activiti.modules.entity.SysDeployNodeEntity;
import com.activiti.modules.entity.TableColumns;
import com.activiti.modules.entity.dto.workflow.DefinitionListDto;
import com.activiti.modules.entity.dto.workflow.DeployProcessDto;
import com.activiti.modules.entity.dto.workflow.FormJsonsDto;
import com.activiti.modules.entity.dto.workflow.TableInfoDto;
import com.activiti.modules.entity.vo.workflow.DefinitionListVo;
import com.activiti.modules.entity.vo.workflow.NodeColumnsVo;
import com.activiti.modules.entity.vo.workflow.TableColumnsVo;
import com.activiti.modules.entity.vo.workflow.TableInfoVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.modules.service.SysDeployNodeService;
import com.activiti.modules.service.SysDeployService;
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
    private SysDeployNodeService deployNodeService;

    @Autowired
    private SysDeployService deployService;

    @Autowired
    private TableDao tableDao;

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
            SysDeployNodeEntity mainForm = deployNodeService.getOne(new LambdaQueryWrapper<SysDeployNodeEntity>()
                    .eq(SysDeployNodeEntity::getDeployId, item.getDeploymentId())
                    .eq(SysDeployNodeEntity::getIsMainFrom, 1));
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

        Date date = new Date();
        // 流程部署详情
        TableInfoDto tableInfo = dto.getTableInfo();
        if (tableInfo != null) {
            SysDeployEntity sysDeploy = new SysDeployEntity();
            sysDeploy.setTableName(tableInfo.getTableName());
            sysDeploy.setTableComment(tableInfo.getTableComment());
            sysDeploy.setDeployId(deploy.getId());
            sysDeploy.setCreateTime(date);
            deployService.save(sysDeploy);
        }

        // 保存节点数据
        List<FormJsonsDto> formJsons = dto.getFormJsonList();
        List<SysDeployNodeEntity> list = new ArrayList<>();
        for (FormJsonsDto formJson : formJsons) {
            SysDeployNodeEntity deployNode = new SysDeployNodeEntity() {{
                setDeployId(deploy.getId());
                setActivityId(formJson.getActivityId());
                setFormJson(formJson.getFormJson());
                setIsMainFrom(formJson.getIsMainFrom());
                setCreateTime(date);
            }};
            dto.getNodeColumns().stream().filter(t -> t.getActivityId()
                            .equals(formJson.getActivityId())).findAny()
                    .ifPresent(t -> deployNode.setColumns(t.getColumns()));
            list.add(deployNode);
        }
        deployNodeService.saveBatch(list);
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
        List<SysDeployNodeEntity> list = deployNodeService.list(new LambdaQueryWrapper<SysDeployNodeEntity>()
                .select(SysDeployNodeEntity::getActivityId,
                        SysDeployNodeEntity::getFormJson,
                        SysDeployNodeEntity::getColumns,
                        SysDeployNodeEntity::getIsMainFrom)
                .eq(SysDeployNodeEntity::getDeployId, deploymentId));
        result.put("formJsonList", list);

        SysDeployEntity sysDeploy = deployService.getById(deploymentId);
        if (sysDeploy != null) {
            // 表结构信息
            TableInfoVo tableInfo = new TableInfoVo();
            tableInfo.setTableName(sysDeploy.getTableName());
            tableInfo.setTableComment(sysDeploy.getTableComment());
            List<TableColumns> tableColumns = tableDao.tableColumns(sysDeploy.getTableName());
            List<TableColumnsVo> columns = new ArrayList<>();
            for (TableColumns tableColumn : tableColumns) {
                TableColumnsVo tableColumnsVo = new TableColumnsVo();
                BeanUtils.copyProperties(tableColumn, tableColumnsVo);
                columns.add(tableColumnsVo);
            }
            tableInfo.setColumns(columns);
            result.put("tableInfo", tableInfo);

            // 获取节点绑定数据库表字段的数据
            List<NodeColumnsVo> nodeColumnsVos = new ArrayList<>();
            for (SysDeployNodeEntity deployNode : list) {
                if(deployNode.getColumns() == null) continue;
                NodeColumnsVo nodeColumnsVo = new NodeColumnsVo();
                nodeColumnsVo.setActivityId(deployNode.getActivityId());
                nodeColumnsVo.setColumns(deployNode.getColumns());
                nodeColumnsVos.add(nodeColumnsVo);
            }
            result.put("nodeColumns", nodeColumnsVos);

        }
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
        deployNodeService.remove(new LambdaQueryWrapper<SysDeployNodeEntity>()
                .eq(SysDeployNodeEntity::getDeployId, deploymentId));
        deployService.remove(new LambdaQueryWrapper<SysDeployEntity>()
                .eq(SysDeployEntity::getDeployId, deploymentId));
        repositoryService.deleteDeployment(deploymentId, true);
    }
}
