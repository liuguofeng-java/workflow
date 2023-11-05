package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.StartListDto;
import com.activiti.modules.entity.vo.workflow.HistoryRecordVo;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.utils.R;
import com.activiti.utils.TokenUtils;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param definitionId 流程定义id
     * @return 结果
     */
    @GetMapping("start")
    public R<String> start(String definitionId) {
        SysUserEntity user = TokenUtils.getUser();
        processStartService.startProcess(definitionId, user.getUserId());
        return R.ok();
    }

    /**
     * 查询审批近路
     *
     * @param instanceId 流程实例id
     * @return 审批记录
     */
    @GetMapping("getHistoryRecord")
    public R<List<HistoryRecordVo>> getHistoryRecord(String instanceId) {
        List<HistoryRecordVo> list = processStartService.getHistoryRecord(instanceId);
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
