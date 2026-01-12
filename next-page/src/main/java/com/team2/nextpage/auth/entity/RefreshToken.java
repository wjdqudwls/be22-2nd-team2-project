package com.team2.nextpage.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Refresh Token 엔티티
 * 사용자별 Refresh Token 정보를 저장합니다.
 * userEmail을 PK로 사용하여 1:1 매핑됩니다.
 *
 * @author 김태형
 */
@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  /** 사용자 이메일 (PK, users 테이블의 user_email) */
  @Id
  @Column(name = "user_email", nullable = false, length = 100)
  private String userEmail;

  /** Refresh Token 값 */
  @Column(name = "token", nullable = false, length = 500)
  private String token;

  /** 토큰 만료 일시 */
  @Column(name = "expiry_date", nullable = false)
  private LocalDateTime expiryDate;

  /**
   * RefreshToken 빌더
   */
  @Builder
  private RefreshToken(String userEmail, String token, LocalDateTime expiryDate) {
    this.userEmail = userEmail;
    this.token = token;
    this.expiryDate = expiryDate;
  }

  /**
   * Refresh Token 갱신
   * 새로운 토큰과 만료 일시로 업데이트합니다.
   *
   * @param newToken      새로운 Refresh Token
   * @param newExpiryDate 새로운 만료 일시
   */
  public void updateToken(String newToken, LocalDateTime newExpiryDate) {
    this.token = newToken;
    this.expiryDate = newExpiryDate;
  }

  /**
   * 토큰 만료 여부 확인
   *
   * @return 만료되었으면 true
   */
  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiryDate);
  }
}