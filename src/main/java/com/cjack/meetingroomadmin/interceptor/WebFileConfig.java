package com.cjack.meetingroomadmin.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置浏览的端访问服务器的文件时候的路径映射
 */
@Configuration
public class WebFileConfig extends WebMvcConfigurerAdapter {

    @Value("${file.upload.baseServerDir}")
    String baseServerDir;
    @Value("${file.upload.baseClientDir}")
    String baseClientDir;
    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry) {
        registry.addResourceHandler( baseClientDir + "**").addResourceLocations( "file:" + baseServerDir);
        super.addResourceHandlers( registry);
    }

    @Override
    public void addViewControllers( ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/pages/login.html");
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers( registry);
    }
}
