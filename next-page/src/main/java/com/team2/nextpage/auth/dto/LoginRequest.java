package com.team2.nextpage.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 로그인 요청 DTO
 * 클라이언트로부터 이메일과 비밀번호를 전달받습니다.
 *
 * @author 김태형
 * @param userEmail 사용자 이메일 (로그인 ID)
 * @param userPw    사용자 비밀번호
 */
public record LoginRequest(
    @NotBlank(message = "이메일은 필수 입력값입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") String userEmail,

    @NotBlank(message = "비밀번호는 필수 입력값입니다.") @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내여야 합니다.") String userPw) {
}
