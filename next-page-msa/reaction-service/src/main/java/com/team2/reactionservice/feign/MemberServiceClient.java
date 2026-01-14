package com.team2.reactionservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Member Service Feign Client
 * member-service의 사용자 정보를 조회하기 위한 Feign 클라이언트
 *
 * @author Next-Page Team
 */
@FeignClient(name = "MEMBER-SERVICE")
public interface MemberServiceClient {

    /**
     * 사용자 ID로 닉네임 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 닉네임
     */
    @GetMapping("/api/members/{userId}/nickname")
    String getUserNickname(@PathVariable("userId") Long userId);
}
