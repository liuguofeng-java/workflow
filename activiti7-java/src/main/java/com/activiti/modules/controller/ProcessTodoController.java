package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.ProcessTodoDto;
import com.activiti.modules.service.ProcessTodoService;
import com.activiti.utils.TokenUtils;
import com.activiti.utils.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代办流程
 *
 * @author liuguofeng
 * @date 2023/11/04 19:12
 **/
@RequestMapping("processTodo")
@RestController
public class ProcessTodoController {

    @Autowired
    private ProcessTodoService processTodoService;

    /**
     * 查看我代办的流程
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(ProcessTodoDto dto) {
        SysUserEntity user = TokenUtils.getUser();
        dto.setUserId(user.getUserId());
        return processTodoService.queryPage(dto);
    }
}
