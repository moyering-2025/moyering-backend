package com.dev.moyering.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
   
   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedOriginPatterns(Arrays.asList("*"));
      config.setAllowCredentials(true);
      config.addAllowedOriginPattern("*"); // 모든 도메인 허용
      config.addAllowedHeader("*"); // 모든 헤더 허용
      config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST 등)
      config.addExposedHeader("Authorization");
      source.registerCorsConfiguration("/*", config);
      source.registerCorsConfiguration("/*/*", config);
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }

}
