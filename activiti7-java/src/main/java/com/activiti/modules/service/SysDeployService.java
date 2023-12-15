package com.activiti.modules.service;

import com.activiti.modules.entity.SysDeployEntity;
import com.activiti.modules.entity.SysDeployNodeEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 流程部署详情
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-12-15 11:48:02
 */
public interface SysDeployService extends IService<SysDeployEntity> {
    /**
     * 插入数据到数据库表中
     *
     * @param instanceId 实例id
     * @param deployId   部署id
     * @param activityId 流程定义节点唯一标识
     * @param variables  流程变量
     */
    void saveData(String instanceId, String deployId, String activityId, Map<String, Object> variables);

}

