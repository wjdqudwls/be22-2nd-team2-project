package com.team2.reactionservice.feign.controller;

import com.team2.commonmodule.feign.dto.MemberReactionStatsDto;
import com.team2.commonmodule.feign.dto.SentenceReactionInfoDto;
import com.team2.commonmodule.response.ApiResponse;
import com.team2.reactionservice.feign.service.ReactionInternalService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * ReactionInternalController
 *
 * @author 정병진
 */
@Hidden
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class ReactionInternalController {

    private final ReactionInternalService reactionInternalService;

    @GetMapping("/members/{userId}/stats")
    public ResponseEntity<ApiResponse<MemberReactionStatsDto>> getMemberReactionStats(@PathVariable Long userId) {
        MemberReactionStatsDto stats = reactionInternalService.getMemberReactionStats(userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @PostMapping("/reactions/sentences/stats")
    public ResponseEntity<ApiResponse<Map<Long, SentenceReactionInfoDto>>> getSentenceReactions(
            @RequestBody List<Long> sentenceIds,
            @RequestParam(required = false) Long userId) {

        Map<Long, SentenceReactionInfoDto> stats = reactionInternalService
                .getSentenceReactions(sentenceIds, userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
