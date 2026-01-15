package com.team2.storyservice.command.book.service;

import com.team2.commonmodule.feign.MemberServiceClient;
import com.team2.commonmodule.feign.dto.MemberInfoDto;
import com.team2.commonmodule.response.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Member Service Integration with Circuit Breaker
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberIntegrationService {

    private final MemberServiceClient memberServiceClient;

    @CircuitBreaker(name = "default", fallbackMethod = "getNicknameFallback")
    public String getUserNickname(Long userId) {
        ApiResponse<MemberInfoDto> response = memberServiceClient.getMemberInfo(userId);
        if (response != null && response.getData() != null) {
            return response.getData().getUserNicknm();
        }
        return "Unknown Writer";
    }

    public String getNicknameFallback(Long userId, Throwable t) {
        log.error("Member Service unavailable/failed. Fallback for userId: {}. Error: {}", userId, t.getMessage());
        return "Unknown Writer";
    }
}
