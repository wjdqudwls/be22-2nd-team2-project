package com.team2.memberservice.jwt;

import com.team2.memberservice.config.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증을 담당하는 Provider 클래스
 * Access Token과 Refresh Token을 발급하고, 토큰의 유효성을 검증합니다.
 *
 * @author 정진호
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds:3600}") // 기본 1시간
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds:604800}") // 기본 7일
    private long refreshTokenValidityInSeconds;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("JWT Secret Key initialized");
    }

    /**
     * Access Token 생성
     * MSA 환경에서 Gateway가 헤더를 주입할 수 있도록 사용자 정보를 모두 포함
     * 
     * @param authentication 인증 정보
     * @return Access Token
     */
    public String createAccessToken(Authentication authentication) {
        // CustomUserDetails에서 사용자 정보 추출
        Object principal = authentication.getPrincipal();
        String userEmail = authentication.getName();
        String userId = null;
        String nickname = null;
        String role = null;

        log.info("Creating Access Token for user: {}", userEmail);
        log.info("Principal type: {}", principal.getClass().getName());

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            if (userDetails.getMember() != null) {
                userId = String.valueOf(userDetails.getMember().getUserId());
                nickname = userDetails.getMember().getUserNicknm();
                role = userDetails.getMember().getUserRole().name();
                log.info("Details extracted from CustomUserDetails - userId: {}, nickname: {}", userId, nickname);
            } else {
                log.error("CustomUserDetails.getMember() is null!");
            }
        } else {
            log.warn("Principal is NOT CustomUserDetails! It is: {}", principal);
            // 만약 principal이 문자열(email)이라면?
            // 비상 대책: 이메일로라도 DB 조회? (일단 로그만)
        }

        if (userId == null) {
            log.error("CRITICAL: userId is null during token creation for {}", userEmail);
            // throw new RuntimeException("User ID cannot be null for token generation");
            // 일단 에러 던지지 말고 로그만 남겨서 진행 상황 확인 (하지만 Gateway에서 막힐 것임)
        }

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity = new Date(now + (accessTokenValidityInSeconds * 1000));

        return Jwts.builder()
                .subject(userId) // subject는 userId로 설정
                .claim("email", userEmail)
                .claim("nickname", nickname)
                .claim("role", role)
                .claim(AUTHORITIES_KEY, authorities)
                .issuedAt(new Date(now))
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     * 
     * @param authentication 인증 정보
     * @return Refresh Token
     */
    public String createRefreshToken(Authentication authentication) {
        // CustomUserDetails에서 사용자 정보 추출
        Object principal = authentication.getPrincipal();
        String userEmail = authentication.getName();
        String userId = null;

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            userId = String.valueOf(userDetails.getMember().getUserId());
        }

        long now = System.currentTimeMillis();
        Date validity = new Date(now + (refreshTokenValidityInSeconds * 1000));

        return Jwts.builder()
                .subject(userId) // subject는 userId로 설정
                .claim("email", userEmail)
                .issuedAt(new Date(now))
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 토큰으로부터 Authentication 객체 생성
     * MSA 환경에서 subject는 userId이므로 email claim에서 이메일을 추출
     * 
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }

        // MSA: subject는 userId, email은 별도 claim
        String userEmail = claims.get("email", String.class);
        if (userEmail == null) {
            throw new IllegalArgumentException("이메일 정보가 없는 토큰입니다.");
        }

        // UserDetailsService를 통해 실제 CustomUserDetails를 로드
        UserDetails principal = userDetailsService.loadUserByUsername(userEmail);

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    /**
     * 토큰 유효성 검증
     * 
     * @param token JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 토큰에서 Claims 추출
     * 
     * @param token JWT 토큰
     * @return Claims
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰에서 사용자 이메일 추출
     * MSA 환경에서 subject는 userId이므로 email claim에서 추출
     * 
     * @param token JWT 토큰
     * @return 사용자 이메일
     */
    public String getUserEmailFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("email", String.class);
    }

    /**
     * 토큰의 남은 유효 시간 확인 (밀리초)
     * 
     * @param token JWT 토큰
     * @return 남은 시간 (ms)
     */
    public long getTokenExpirationTime(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
}
