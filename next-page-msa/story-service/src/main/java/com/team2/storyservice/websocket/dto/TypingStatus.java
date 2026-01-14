package com.team2.storyservice.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ?낅젰 ?곹깭 硫붿떆吏 DTO
 *
 * @author ?뺤쭊??
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypingStatus {

    private Long bookId;
    private String userNickname;
    private boolean isTyping;

    public static TypingStatus startTyping(Long bookId, String userNickname) {
        return new TypingStatus(bookId, userNickname, true);
    }

    public static TypingStatus stopTyping(Long bookId, String userNickname) {
        return new TypingStatus(bookId, userNickname, false);
    }
}
