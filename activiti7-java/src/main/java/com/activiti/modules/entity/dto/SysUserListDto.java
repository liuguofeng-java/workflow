package com.activiti.modules.entity.dto;

import lombok.Data;

/**
 * 用户信息列表参数
 *
 * @author liuguofeng
 * @date 2023/10/20 09:48
 **/
@Data
public class SysUserListDto {
    // 用户名称
    private String userName;

    // 手机号
    private String mobile;

    // 邮箱
    private String email;

    // 注册开始时间
    private String startTime;

    // 注册结束时间
    private String endTime;
}
