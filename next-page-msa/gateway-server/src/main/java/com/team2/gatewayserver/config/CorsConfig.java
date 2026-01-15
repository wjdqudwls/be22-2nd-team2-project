package com.team2.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Gateway Server CORS Configuration
 * 전역 CORS 설정을 통해 프론트엔드와 백엔드 간 통신 허용
 *
 * @author Next-Page Team
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 출처 (Origin)
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*", // 로컬 개발 환경
                "http://127.0.0.1:*", // 로컬 개발 환경
                "http://192.168.*.*:*" // 로컬 네트워크 환경
        ));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // 허용할 헤더
        config.setAllowedHeaders(Arrays.asList("*"));

        // 인증 정보 포함 허용 (쿠키, Authorization 헤더 등)
        config.setAllowCredentials(true);

        // Preflight 요청 결과 캐시 시간 (초)
        config.setMaxAge(3600L);

        // 노출할 응답 헤더
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "X-User-Id",
                "X-User-Email",
                "X-User-Nickname",
                "X-User-Role"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
