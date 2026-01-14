package com.team2.storyservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ??臾몄옣 ?묒꽦 ?대깽??DTO
 *
 * @author ?뺤쭊??
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
