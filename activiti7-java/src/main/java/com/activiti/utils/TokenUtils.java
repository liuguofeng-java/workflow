package com.activiti.utils;

import com.activiti.modules.entity.SysUserEntity;
import com.activiti.modules.service.SysUserService;
import com.activiti.utils.constant.Constants;
import com.activiti.utils.exception.AException;
import org.apache.commons.lang3.StringUtils;

/**
 * token工具类
 *
 * @author liuguofeng
 * @date 2023/11/04 12:33
 **/
public class TokenUtils {

    /**
     * 假装登录获取用户信息
     *
     * @return 用户信息
     */
    public static SysUserEntity getUser() {
        SysUserService userService = SpringUtils.getBean(SysUserService.class);

        // 在请求头获取用户id
        String userId = ServletUtils.getHeader(Constants.TOKEN);
        if (StringUtils.isEmpty(userId)) {
            throw new AException("未登录！", 401);
        }
        SysUserEntity user = userService.getById(userId);
        if (user == null) {
            throw new AException("未知用户！", 401);
        }
        return user;
    }
}
