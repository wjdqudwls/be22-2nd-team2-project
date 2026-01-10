package com.team2.nextpage.command.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 *
 * @author 김태형
 */
@Getter
@NoArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
}
