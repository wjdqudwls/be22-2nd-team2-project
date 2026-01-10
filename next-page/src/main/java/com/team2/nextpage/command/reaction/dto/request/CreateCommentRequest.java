package com.team2.nextpage.command.reaction.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 작성 요청 DTO
 *
 * @author 정병진
 */
@Getter
@NoArgsConstructor
public class CreateCommentRequest {
    private Long bookId;
    private String content;
}
