package com.activiti.modules.service.impl;

import com.activiti.modules.entity.dto.ProcessDefinitionListDto;
import com.activiti.modules.entity.vo.ProcessDefinitionVo;
import com.activiti.modules.service.ProcessDefinitionService;
import com.activiti.utils.page.PageDomain;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程管理实现
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
    public TableDataInfo queryPage(ProcessDefinitionListDto dto) {
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
     * 删除流程
     *
     * @param id 部署id
     */
    @Override
    public void delete(String id) {
        repositoryService.deleteDeployment(id, true);
    }
}
