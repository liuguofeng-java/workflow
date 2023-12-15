package com.activiti.modules.service.impl;

import com.activiti.modules.dao.SysDeployDao;
import com.activiti.modules.entity.SysDeployEntity;
import com.activiti.modules.entity.SysDeployNodeEntity;
import com.activiti.modules.service.SysDeployNodeService;
import com.activiti.modules.service.SysDeployService;
import com.activiti.modules.service.TableService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysDeployService")
public class SysDeployServiceImpl extends ServiceImpl<SysDeployDao, SysDeployEntity> implements SysDeployService {

    @Autowired
    private SysDeployNodeService deployNodeService;

    @Autowired
    private TableService tableService;

    /**
     * 插入数据到数据库表中
     *
     * @param instanceId 实例id
     * @param deployId   部署id
     * @param activityId 流程定义节点唯一标识
     * @param variables  流程变量
     */
    @Override
    public void saveData(String instanceId, String deployId, String activityId, Map<String, Object> variables) {
        SysDeployEntity sysDeploy = this.getById(deployId);
        // 如果没有找到数据就说明这个流程没有绑定数据库表
        if (sysDeploy == null) return;
        SysDeployNodeEntity deployNode = deployNodeService.getOne(new LambdaQueryWrapper<SysDeployNodeEntity>()
                .eq(SysDeployNodeEntity::getActivityId, activityId)
                .eq(SysDeployNodeEntity::getDeployId, deployId));
        if (deployNode != null && deployNode.getColumns() != null) {
            // 更新数据
            tableService.saveOrUpdateData(instanceId, sysDeploy.getTableName(),
                    deployNode.getColumns(), variables);
        }
    }
}