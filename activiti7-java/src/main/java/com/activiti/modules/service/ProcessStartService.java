package com.activiti.modules.service;


import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.utils.page.TableDataInfo;

import java.util.List;

/**
 * 流程启动
 *
 * @author liuguofeng
 * @date 2023/11/04 12:07
 **/
public interface ProcessStartService {

    /**
     * 我发起的任务列表
     *
     * @param dto 参数
     * @return 结果
     */
    TableDataInfo queryPage(StartListDto dto);

    /**
     * 启动流程
     *
     * @param definitionId 流程定义id
     * @param userId       当前用户登录id
     */
    void startProcess(String definitionId, String userId);

    /**
     * 查询审批近路
     *
     * @param instanceId 流程实例id
     * @return 审批记录
     */
    List<HistoryRecordVo> getHistoryRecord(String instanceId);

    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例id
     */
    void delete(String instanceId);

}
