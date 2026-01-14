package com.team2.reactionservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 투표 업데이트 DTO
 *
 * @author Next-Page Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteUpdateDto {
    private Long targetId;
    private String targetType; // "BOOK" or "SENTENCE"
    private Long upvotes;
    private Long downvotes;
}
