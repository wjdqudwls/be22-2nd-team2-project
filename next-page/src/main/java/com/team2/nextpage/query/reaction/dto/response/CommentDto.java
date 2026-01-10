package com.team2.nextpage.query.reaction.dto.response;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 댓글 조회용 DTO
 *
 * @author 정병진
 */
@Getter
public class CommentDto {
    private Long commentId;
    private String content;
    private String writerNicknm;
    private LocalDateTime createdAt;
}
