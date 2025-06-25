package com.dev.moyering.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final VisitorInterceptor visitorInterceptor;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // iupload 폴더의 파일들을 정적 파일로 서빙
//        registry.addResourceHandler("/iupload/**")
//                .addResourceLocations("file:iupload/");
//    }


    @Value("${iupload.path}")
    private String iuploadPath;  // e.g. C:/khj/springboot-workspace/iupload

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/iupload/**")
                .addResourceLocations("file:" + iuploadPath + "/");
    }

    // 방문자 로깅
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/static/**",
                        "/favicon.ico",
                        "/image",
                        "/iupload/**",
                        "/*.png",
                        "/*.css",
                        "/*.js"
                );
    }
    // 쿠키 허용
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:5173") // 프론트 주소
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowCredentials(true); // 쿠키 허용!
//    }
}
