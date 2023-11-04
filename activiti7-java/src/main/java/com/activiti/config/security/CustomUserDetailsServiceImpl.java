package com.activiti.config.security;

import com.activiti.modules.entity.SysUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 因为activiti7默认使用security,在查询任务报错,要实现此类才能解决,或者当前框架真正使用security
 *
 * @author liuguofeng
 * @date 2023/11/04 20:26
 **/
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询数据库获取权限(我这里伪造)
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        SysUserEntity user = new SysUserEntity();
        user.setUsername(username);
        user.setAuthorities(authorities);
        return user;
    }
}
