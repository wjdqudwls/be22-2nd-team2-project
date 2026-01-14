package com.team2.storyservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ???뚯꽕 ?앹꽦 ?대깽??DTO
 *
 * @author ?뺤쭊??
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
