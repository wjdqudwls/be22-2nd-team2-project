package com.team2.nextpage.command.member.controller;

import com.team2.nextpage.command.member.service.MemberService;
import com.team2.nextpage.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 Command 컨트롤러
 *
 * @author 김태형
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signUp(/* @RequestBody SignUpRequest request */) {
        Long userId = memberService.signUp(/* request */);
        return ResponseEntity.ok(ApiResponse.success(userId));
    }
}
