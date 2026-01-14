package com.team2.memberservice.api;

import com.team2.memberservice.command.member.entity.Member;
import com.team2.memberservice.command.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member API Controller (For Inter-Service Communication)
 * 다른 마이크로서비스에서 호출하는 API 엔드포인트
 *
 * @author Next-Page Team
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberRepository memberRepository;

    /**
     * 사용자 닉네임 조회 (Feign Client용)
     *
     * @param userId 사용자 ID
     * @return 사용자 닉네임 (존재하지 않으면 "Unknown")
     */
    @GetMapping("/{userId}/nickname")
    public String getUserNickname(@PathVariable Long userId) {
        return memberRepository.findById(userId)
                .map(Member::getUserNicknm)
                .orElse("Unknown");
    }
}
