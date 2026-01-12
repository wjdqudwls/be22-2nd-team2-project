package com.team2.nextpage.auth.controller;

import com.team2.nextpage.auth.dto.LoginRequest;
import com.team2.nextpage.auth.dto.TokenResponse;
import com.team2.nextpage.auth.service.AuthService;
import com.team2.nextpage.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * 인증(Auth) 컨트롤러
 * 로그인, 토큰 갱신, 로그아웃 등 인증/인가 관련 엔드포인트를 제공합니다.
 *
 * @author 김태형
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  /**
   * 로그인
   * 사용자 이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.
   *
   * @param loginRequest 로그인 요청 DTO (userEmail, userPw)
   * @return Access Token (Body) + Refresh Token (Cookie)
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenResponse>> login(
      @RequestBody @Valid LoginRequest loginRequest) {
    TokenResponse response = authService.login(loginRequest);
    return buildTokenResponse(response);
  }

  /**
   * 토큰 갱신
   * Refresh Token을 이용하여 새로운 Access Token과 Refresh Token을 발급받습니다.
   *
   * @param refreshToken 쿠키에서 전달받은 Refresh Token
   * @return 새로운 Access Token (Body) + Refresh Token (Cookie)
   */
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
    // 쿠키에 refresh token이 없을 경우
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // refresh token이 유효하면 새 access/refresh token 발급
    TokenResponse tokenResponse = authService.refreshToken(refreshToken);
    return buildTokenResponse(tokenResponse);
  }

  /**
   * 로그아웃
   * Refresh Token을 무효화하고 쿠키를 삭제합니다.
   *
   * @param refreshToken 쿠키에서 전달받은 Refresh Token
   * @return 성공 응답
   */
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout(
      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
    if (refreshToken != null) {
      authService.logout(refreshToken);
    }
    ResponseCookie deleteCookie = createDeleteRefreshTokenCookie();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
        .body(ApiResponse.success());
  }

  /**
   * Access Token과 Refresh Token을 body와 쿠키에 담아 반환
   */
  private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse) {
    ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.success(tokenResponse));
  }

  /**
   * Refresh Token 쿠키 생성
   */
  private ResponseCookie createRefreshTokenCookie(String refreshToken) {
    return ResponseCookie.from("refreshToken", refreshToken)
        .httpOnly(true) // HttpOnly 속성 설정 (JavaScript에서 접근 불가)
        // .secure(true) // HTTPS 환경일 때만 전송 (운영 환경에서 활성화 권장)
        .path("/") // 쿠키 범위: 전체 경로
        .maxAge(Duration.ofDays(7)) // 쿠키 만료 기간: 7일
        .sameSite("Strict") // CSRF 공격 방어를 위한 SameSite 설정
        .build();
  }

  /**
   * Refresh Token 삭제용 쿠키 생성 (maxAge = 0)
   */
  private ResponseCookie createDeleteRefreshTokenCookie() {
    return ResponseCookie.from("refreshToken", "")
        .httpOnly(true)
        .path("/")
        .maxAge(0) // 즉시 만료
        .sameSite("Strict")
        .build();
  }
}
