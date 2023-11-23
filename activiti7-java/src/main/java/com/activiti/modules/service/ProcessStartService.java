package com.activiti.modules.service;


import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.dto.workflow.StartProcessDto;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.utils.page.TableDataInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * @param dto    启动流程参数
     * @param userId 当前用户登录id
     */
    void startProcess(StartProcessDto dto, String userId);

    /**
     * 查询审批进度
     *
     * @param instanceId 流程实例id
     * @return 审批记录
     */
    List<HistoryRecordVo> getHistoryRecord(String instanceId);

    /**
     * 查询流程图信息(高亮信息)
     *
     * @param instanceId 流程实例id
     * @return 流程图信息
     */
    Map<String, Object> getHighlightNodeInfo(String instanceId);

    /**
     * 获取主表单信息
     *
     * @param instanceId 流程实例id
     * @return 主表单数据
     */
    Map<String, Object> getMainFormInfo(String instanceId);

    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例id
     */
    void delete(String instanceId);

}
