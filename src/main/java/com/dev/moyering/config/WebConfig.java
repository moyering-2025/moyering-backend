package com.dev.moyering.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${iupload.path}")
    private String iuploadPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // iupload 폴더의 파일들을 정적 파일로 서빙
        registry.addResourceHandler("/iupload/**")
                .addResourceLocations("file:" + iuploadPath + "/");
    }
}
