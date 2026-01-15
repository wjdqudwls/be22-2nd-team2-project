package com.team2.storyservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 새 소설 생성 이벤트 DTO
 *
 * @author 정진호
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreatedEvent {
    private Long bookId;
    private String title;
    private String categoryName;
    private String writerNickname;
}
