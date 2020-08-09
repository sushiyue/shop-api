package com.fh.shop.api.config;

import com.fh.shop.api.interceptor.LoginInterceptor;
import com.fh.shop.api.interceptor.miInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        public  void addInterceptors(InterceptorRegistry registry){
            registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/**");
            registry.addInterceptor(miInterceptors()).addPathPatterns("/api/**");
        }
        @Bean
        public LoginInterceptor loginInterceptor(){
             return new LoginInterceptor();
        }

    @Bean
    public miInterceptor miInterceptors(){
        return new miInterceptor();
    }
}
