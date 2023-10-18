package com.activiti.modules.entity.dto;

import lombok.Data;

/**
 * 登录参数
 *
 * @author liuguofeng
 * @date 2023/10/18 11:12
 **/
@Data
public class LoginUserDto {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
