package com.team2.commonmodule.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * SentenceReactionInfoDto
 *
 * @author Next-Page Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceReactionInfoDto {
    private Long sentenceId;
    private long likeCount;
    private long dislikeCount;
    private String myVote; // "LIKE", "DISLIKE", or null
}
