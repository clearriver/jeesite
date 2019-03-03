/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Application
 * 
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringBootApplication
@EnableSwagger2
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Application.class);
	}

	  @SuppressWarnings("rawtypes")
	  @Bean
	  public Docket api() {
	    return new Docket(DocumentationType.SWAGGER_2).select()
	        .apis(
	            RequestHandlerSelectors.basePackage("com.jeesite.modules.restful"))
	        .paths(PathSelectors.any()).build()
	        .apiInfo(new ApiInfo("Video Service Api Documentation",
	            "Documentation automatically generated", "V1", null,
	            new Contact("Video System", "www.july7.top", "wuqinghe2005@126.com"), null, null,
	            new ArrayList<VendorExtension>()))
	        .genericModelSubstitutes(Optional.class);
	  }

	  @Bean
	  public RestTemplate getRestTemplate(RestTemplateBuilder builder){
		  return builder.build();
	  }

	  @Bean
	  public Gson createGson() {
		  return new Gson();
//		  return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy(){
//			@Override
//			public boolean shouldSkipField(FieldAttributes f) {
//				return false;
//			}
//
//			@Override
//			public boolean shouldSkipClass(Class<?> clazz) {
//				return false;
//			}
//		  }).create();
	  }
}