/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.rdap.common.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * spring security的核心配置类
 *
 * @see SecurityExpressionRoot 校验的方法
 * @author tuhanbao
 *         下午2:42:52
 */
@EnableWebSecurity
@AutoConfiguration
// 允许使用@PreAuthorize对方法进行权限控制，
// @see SecurityExpressionRoot 校验的方法
public class AppWebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}