package com.team2.commonmodule.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book 투표 정보 DTO (Feign Client용)
 *
 * @author Next-Page Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReactionInfoDto {
    private Long bookId;
    private long likeCount;
    private long dislikeCount;
    private String myVote; // LIKE, DISLIKE, or null
}
