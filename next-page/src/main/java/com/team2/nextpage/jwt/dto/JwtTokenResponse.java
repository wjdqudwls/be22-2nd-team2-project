package com.team2.nextpage.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * JWT 토큰 응답 DTO
 * 로그인 성공 시 클라이언트에게 반환되는 토큰 정보
 *
 * @author 정진호
 */
@Getter
@Builder
@AllArgsConstructor
public class JwtTokenResponse {

    /**
     * 토큰 타입 (항상 "Bearer")
     */
    private String grantType;

    /**
     * Access Token (단기 유효, API 호출용)
     */
    private String accessToken;

    /**
     * Refresh Token (장기 유효, 토큰 갱신용)
     */
    private String refreshToken;

    /**
     * Access Token 만료 시간 (초)
     */
    private Long accessTokenExpiresIn;

    /**
     * 사용자 정보 (선택사항)
     */
    private UserInfo userInfo;

    /**
     * 사용자 기본 정보
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String email;
        private String nickname;
        private String role;
    }

    /**
     * Bearer 토큰 형식으로 생성하는 정적 팩토리 메서드
     */
    public static JwtTokenResponse of(
            String accessToken,
            String refreshToken,
            Long expiresIn,
            UserInfo userInfo) {
        return JwtTokenResponse.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(expiresIn)
                .userInfo(userInfo)
                .build();
    }
}
