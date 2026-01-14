package com.team2.memberservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증을 처리하는 Filter
 * 요청 헤더에서 JWT 토큰을 추출하고 검증하여 SecurityContext에 인증 정보를 설정합니다.
 *
 * @author 정진호
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 필터 실행 메서드
     * HTTP 요청마다 한 번씩 실행되며, JWT 토큰을 검증하고 인증 정보를 설정
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검증
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            try {
                // 3. 토큰으로부터 Authentication 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 4. SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. URI: {}",
                        authentication.getName(), request.getRequestURI());
            } catch (Exception e) {
                log.error("JWT 토큰 인증 과정에서 오류가 발생했습니다: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        } else {
            log.debug("유효한 JWT 토큰이 없습니다. URI: {}", request.getRequestURI());
        }

        // 5. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보 추출
     * 
     * @param request HTTP 요청
     * @return JWT 토큰 문자열 (Bearer 제거)
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * 특정 경로에 대해 필터를 적용하지 않도록 설정 가능
     * 필요시 오버라이드하여 사용
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Swagger UI, actuator 등 인증이 필요 없는 경로
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/actuator") ||
                path.startsWith("/h2-console");
    }
}
