package com.team2.commonmodule.feign;

import com.team2.commonmodule.feign.dto.MemberReactionStatsDto;
import com.team2.commonmodule.feign.dto.SentenceReactionInfoDto;
import com.team2.commonmodule.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * ReactionServiceClient
 *
 * @author Next-Page Team
 */
@FeignClient(name = "reaction-service")
public interface ReactionServiceClient {

        @PostMapping("/internal/reactions/sentences/stats")
        ApiResponse<Map<Long, SentenceReactionInfoDto>> getSentenceReactions(
                        @RequestBody List<Long> sentenceIds,
                        @RequestParam(value = "userId", required = false) Long userId);

        @GetMapping("/internal/members/{userId}/stats")
        ApiResponse<MemberReactionStatsDto> getMemberReactionStats(
                        @PathVariable("userId") Long userId);
}
