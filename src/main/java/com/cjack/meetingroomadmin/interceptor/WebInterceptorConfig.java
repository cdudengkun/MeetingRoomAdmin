package com.cjack.meetingroomadmin.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by root on 3/3/19.
 */
@Configuration
public class WebInterceptorConfig  extends WebMvcConfigurerAdapter {

    //关键，将拦截器作为bean写入配置中才能在拦截器里面注入spring的其它类
    @Bean
    public CheckLoginInterceptor checkCookieInterceptor(){
        return new CheckLoginInterceptor();
    }

    /**
     * 注册 拦截器
     */
    @Override
    public void addInterceptors( InterceptorRegistry registry) {
        //排除不需要登录的接口
        registry.addInterceptor( checkCookieInterceptor()).excludePathPatterns(
                "/adminUser/login").excludePathPatterns( "/dictionary/scanWordAudio");
    }
}
