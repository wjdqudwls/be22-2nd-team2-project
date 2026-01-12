package com.team2.nextpage.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * 토큰 응답 DTO
 * 로그인 및 토큰 갱신 시 Access Token과 Refresh Token을 반환합니다.
 *
 * @author 김태형
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {

  /** Access Token (Body로 전달) */
  private final String accessToken;

  /** Refresh Token (Cookie로도 전달됨) */
  private final String refreshToken;

  /** Token 타입 (기본: Bearer) */
  @Builder.Default
  private final String tokenType = "Bearer";
}
