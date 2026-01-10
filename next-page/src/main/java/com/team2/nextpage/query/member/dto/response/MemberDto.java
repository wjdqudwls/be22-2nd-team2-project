package com.team2.nextpage.query.member.dto.response;

import lombok.Getter;

/**
 * 회원 조회용 DTO
 *
 * @author 김태형
 */
@Getter
public class MemberDto {
    private Long userId;
    private String userEmail;
    private String userNicknm;
    // other fields for view
}
