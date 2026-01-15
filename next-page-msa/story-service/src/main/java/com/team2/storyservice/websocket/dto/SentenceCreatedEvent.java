package com.team2.storyservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 새 문장 작성 이벤트 DTO
 *
 * @author 정진호
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SentenceCreatedEvent {
    private Long bookId;
    private Long sentenceId;
    private String content;
    private Integer sequenceNo;
    private String writerNickname;
}
