package com.team2.reactionservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Story Service Feign Client
 * story-service의 책/문장 정보를 조회하기 위한 Feign 클라이언트
 *
 * @author Next-Page Team
 */
@FeignClient(name = "STORY-SERVICE")
public interface StoryServiceClient {

    /**
     * 문장 ID로 해당 책의 ID 조회
     *
     * @param sentenceId 문장 ID
     * @return 책 ID
     */
    @GetMapping("/api/sentences/{sentenceId}/book-id")
    Long getBookIdBySentenceId(@PathVariable("sentenceId") Long sentenceId);
}
