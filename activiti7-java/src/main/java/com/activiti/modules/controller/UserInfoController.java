package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.UserInfoListDto;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.R;
import com.activiti.utils.exception.AException;
import com.activiti.utils.page.PageUtils;
import com.activiti.utils.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息维护
 *
 * @author liuguofeng
 * @date 2023/10/18 11:03
 **/
@RequestMapping("userInfo")
@RestController
public class UserInfoController {
    @Autowired
    private SysUserService userService;

    /**
     * 列表
     *
     * @param dto 参数
     */
    @GetMapping("list")
    public TableDataInfo list(UserInfoListDto dto) {
        PageUtils.startPage();
        List<SysUserEntity> list = userService.queryPage(dto);
        return PageUtils.getDataTable(list);
    }
}
