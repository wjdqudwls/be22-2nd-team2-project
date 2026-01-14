package com.team2.storyservice.websocket.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ?ㅼ떆媛??볤? ?앹꽦 ?대깽??DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedEvent {
    private Long commentId;
    private Long bookId;
    private Long writerId;
    private String writerNicknm;
    private String content;
    private LocalDateTime createdAt;
}
