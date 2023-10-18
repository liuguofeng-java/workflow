package com.activiti.modules.controller;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.entity.dto.LoginUserDto;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.R;
import com.activiti.utils.exception.AException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 *
 * @author liuguofeng
 * @date 2023/10/18 11:03
 **/
@RestController
public class LoginController {
    @Autowired
    private SysUserService userService;

    /**
     * 模拟登录
     *
     * @param dto 参数
     */
    @PostMapping("login")
    public R<SysUserEntity> login(@RequestBody LoginUserDto dto) {
        SysUserEntity user = userService.getOne(new LambdaQueryWrapper<SysUserEntity>()
                .eq(SysUserEntity::getUsername, dto.getUserName())
                .eq(SysUserEntity::getPassword, dto.getPassword()));
        if (user == null) {
            throw new AException("用户或者密码错误!");
        }
        return R.ok(user);
    }
}
