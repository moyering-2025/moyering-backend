package com.dev.moyering.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
   
   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true); // 프론트에서 쿠키 허용 시 필요
      config.addAllowedOriginPattern("*"); // 모든 도메인 허용
      config.addAllowedHeader("*"); // 모든 헤더 허용
      config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST 등)
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }
}
