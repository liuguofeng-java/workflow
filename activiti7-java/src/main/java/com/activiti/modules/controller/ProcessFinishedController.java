package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.workflow.FinishedListDto;
import com.activiti.modules.service.ProcessHistoryService;
import com.activiti.utils.TokenUtils;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 已办任务
 *
 * @author liuguofeng
 * @date 2024/01/08 9:12
 **/
@RequestMapping("processFinished")
@RestController
public class ProcessFinishedController {

    @Autowired
    private ProcessHistoryService processHistoryService;

    /**
     * 查看我代办的流程
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(FinishedListDto dto) {
        SysUserEntity user = TokenUtils.getUser();
        dto.setUserId(user.getUserId());
        return processHistoryService.queryPage(dto);
    }


}
