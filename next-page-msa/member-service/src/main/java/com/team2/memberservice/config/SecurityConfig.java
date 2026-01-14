package com.team2.memberservice.config;

import com.team2.memberservice.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 * JWT 기반 인증/인가 처리 및 엔드포인트별 접근 권한 관리
 *
 * @author 정진호
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * 비밀번호 암호화를 위한 Encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 인증 관리자 (로그인 처리용)
   * AuthService에서 로그인 시 사용
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Security Filter Chain 설정
   * - JWT 기반 Stateless 세션 관리
   * - 엔드포인트별 접근 권한 제어
   * - CSRF 비활성화 (REST API는 CSRF 불필요)
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF 비활성화 (JWT 사용으로 불필요)
        .csrf(AbstractHttpConfigurer::disable)

        // 세션 정책: STATELESS (JWT 사용)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 엔드포인트별 인증/인가 규칙
        .authorizeHttpRequests(auth -> auth
            // 1. 관리자 전용 API - ADMIN 역할 필수 (가장 먼저 체크)
            .requestMatchers("/api/auth/admin/users/**").hasRole("ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")

            // 2. 인증 관련 API - 누구나 접근 가능
            .requestMatchers("/api/auth/signup", "/api/auth/admin", "/api/auth/login", "/api/auth/check-email",
                "/api/auth/check-nickname")
            .permitAll()
            .requestMatchers("/api/auth/refresh", "/api/auth/logout").permitAll()

            // View Pages
            .requestMatchers("/", "/mypage", "/books/**", "/websocket-test").permitAll()

            // WebSocket endpoints - MUST be permitAll
            .requestMatchers("/ws/**").permitAll()

            // 3. 소설 조회 API - GET 요청만 누구나 접근 가능
            .requestMatchers(HttpMethod.GET, "/api/books").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/books/{bookId}").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/books/{bookId}/view").permitAll()

            // 4. 카테고리 조회 API - GET 요청만 누구나 접근 가능
            .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

            // 5. 댓글 조회 API - GET 요청만 누구나 접근 가능
            .requestMatchers(HttpMethod.GET, "/api/reactions/comments/{bookId}").permitAll()

            // 6. 에러 및 정적 리소스
            .requestMatchers("/error", "/favicon.ico", "/css/**", "/js/**", "/images/**", "/static/**").permitAll()

            // 7. Swagger/OpenAPI 문서 접근 허용
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**")
            .permitAll()

            // 8. 개발 도구 (H2 Console, Actuator 등)
            .requestMatchers(
                "/h2-console/**",
                "/actuator/**")
            .permitAll()

            // 9. 나머지 모든 요청(POST, PUT, PATCH, DELETE 등)은 인증 필요
            .anyRequest().authenticated())

        // Form Login / HTTP Basic 비활성화
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // JWT 인증 필터 추가
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)

        // H2 Console 사용을 위한 설정 (개발 환경)
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()));

    return http.build();
  }
}
