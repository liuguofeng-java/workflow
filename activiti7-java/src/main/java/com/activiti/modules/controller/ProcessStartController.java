package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.dto.workflow.StartProcessDto;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.modules.service.ProcessHistoryService;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.utils.R;
import com.activiti.utils.TokenUtils;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程启动
 *
 * @author liuguofeng
 * @date 2023/11/04 12:07
 **/
@RequestMapping("processStart")
@RestController
public class ProcessStartController {
    @Autowired
    private ProcessStartService processStartService;

    @Autowired
    private ProcessHistoryService processHistoryService;
    /**
     * 我发起的任务列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(StartListDto dto) {
        SysUserEntity user = TokenUtils.getUser();
        dto.setUserId(user.getUserId());
        return processStartService.queryPage(dto);
    }

    /**
     * 根据流程定义id 启动流程
     *
     * @param dto 启动流程参数
     * @return 结果
     */
    @PostMapping("start")
    public R<String> start(@RequestBody StartProcessDto dto) {
        SysUserEntity user = TokenUtils.getUser();
        processStartService.startProcess(dto, user.getUserId());
        return R.ok();
    }

    /**
     * 查询审批进度
     *
     * @param instanceId 流程实例id
     * @return 审批记录
     */
    @GetMapping("getHistoryRecord")
    public R<List<HistoryRecordVo>> getHistoryRecord(String instanceId) {
        List<HistoryRecordVo> list = processHistoryService.getHistoryRecord(instanceId);
        return R.ok(list);
    }

    /**
     * 查询流程图信息(高亮信息)
     *
     * @param instanceId 流程实例id
     * @return 流程图信息
     */
    @GetMapping("getHighlightNodeInfo")
    public R<Map<String, Object>> getHighlightNodeInfo(String instanceId) {
        Map<String, Object> result = processHistoryService.getHighlightNodeInfo(instanceId);
        return R.ok(result);
    }

    /**
     * 获取主表单信息
     *
     * @param instanceId 流程实例id
     * @return 主表单数据
     */
    @GetMapping("getMainFormInfo")
    public R<Map<String,Object>> getMainFormInfo(String instanceId) {
        Map<String,Object> list = processHistoryService.getMainFormInfo(instanceId);
        return R.ok(list);
    }

    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例id
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String instanceId) {
        processStartService.delete(instanceId);
        return R.ok();
    }
}
