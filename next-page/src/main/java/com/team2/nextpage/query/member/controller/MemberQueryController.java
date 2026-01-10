package com.team2.nextpage.query.member.controller;

import com.team2.nextpage.query.member.dto.response.MemberDto;
import com.team2.nextpage.query.member.service.MemberQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 Query 컨트롤러
 *
 * @author 김태형
 */
@RestController
@RequestMapping("/api/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    public MemberQueryController(MemberQueryService memberQueryService) {
        this.memberQueryService = memberQueryService;
    }

    /**
     * 마이페이지 조회 API
     */
    @GetMapping("/me")
    public MemberDto getMyInfo() {
        return null; // impl
    }
}
