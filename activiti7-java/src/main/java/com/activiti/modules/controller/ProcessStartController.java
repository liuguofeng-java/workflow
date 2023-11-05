package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.ProcessStartListDto;
import com.activiti.modules.service.ProcessStartService;
import com.activiti.utils.R;
import com.activiti.utils.TokenUtils;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public TableDataInfo list(ProcessStartListDto dto) {
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
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("delete")
    public R<String> delete(@RequestBody String id) {
        processStartService.delete(id);
        return R.ok();
    }
}
