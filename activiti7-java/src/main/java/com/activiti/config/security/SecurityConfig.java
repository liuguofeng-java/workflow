package com.activiti.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 该配置跳过spring-security的验证
 *
 * @author liuguofeng
 * @date 2023/10/17 10:52
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}