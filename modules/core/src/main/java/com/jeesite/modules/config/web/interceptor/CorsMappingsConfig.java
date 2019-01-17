/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 后台管理日志记录拦截器
 * @author ThinkGem
 * @version 2018年1月10日
 */
@Configuration
@EnableWebMvc
//@ConditionalOnProperty(name="web.restful.api.enabled", havingValue="true", matchIfMissing=true)
public class CorsMappingsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/swagger-ui.html").allowedMethods("PUT","DELETE","GET","POST","PATCH","OPTIONS").allowedOrigins("*").allowCredentials(true);
		registry.addMapping("/swagger-resources/**").allowedMethods("PUT","DELETE","GET","POST","PATCH","OPTIONS").allowedOrigins("*").allowCredentials(true);
		registry.addMapping("/webjars/**").allowedMethods("PUT","DELETE","GET","POST","PATCH","OPTIONS").allowedOrigins("*").allowCredentials(true);
		registry.addMapping("/v2/api-docs").allowedMethods("PUT","DELETE","GET","POST","PATCH","OPTIONS").allowedOrigins("*").allowCredentials(true);
		registry.addMapping("/api/**").allowedMethods("PUT","DELETE","GET","POST","PATCH","OPTIONS").allowedOrigins("*").allowCredentials(true);
	}
}