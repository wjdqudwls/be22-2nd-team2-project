package com.team2.storyservice.websocket.controller;

import com.team2.storyservice.websocket.dto.TypingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * WebSocket 硫붿떆吏 泥섎━ 而⑦듃濡ㅻ윭
 * ?ㅼ떆媛??낅젰 ?곹깭 釉뚮줈?쒖틦?ㅽ듃
 *
 * @author ?뺤쭊??
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class TypingController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * ?낅젰 ?쒖옉 ?대깽??泥섎━
     * ?대씪?댁뼵?? /app/typing/start
     * 釉뚮줈?쒖틦?ㅽ듃: /topic/typing/{bookId}
     */
    @MessageMapping("/typing/start")
    public void handleTypingStart(TypingStatus status) {
        status.setTyping(true);
        log.debug("Typing started - Book: {}, User: {}", status.getBookId(), status.getUserNickname());
        messagingTemplate.convertAndSend("/topic/typing/" + status.getBookId(), status);
    }

    /**
     * ?낅젰 醫낅즺 ?대깽??泥섎━
     * ?대씪?댁뼵?? /app/typing/stop
     * 釉뚮줈?쒖틦?ㅽ듃: /topic/typing/{bookId}
     */
    @MessageMapping("/typing/stop")
    public void handleTypingStop(TypingStatus status) {
        status.setTyping(false);
        log.debug("Typing stopped - Book: {}, User: {}", status.getBookId(), status.getUserNickname());
        messagingTemplate.convertAndSend("/topic/typing/" + status.getBookId(), status);
    }

    /**
     * ?볤? ?낅젰 ?쒖옉 ?대깽??泥섎━
     * ?대씪?댁뼵?? /app/comment-typing/start
     * 釉뚮줈?쒖틦?ㅽ듃: /topic/comment-typing/{bookId}
     */
    @MessageMapping("/comment-typing/start")
    public void handleCommentTypingStart(TypingStatus status) {
        status.setTyping(true);
        log.debug("Comment typing started - Book: {}, User: {}", status.getBookId(), status.getUserNickname());
        messagingTemplate.convertAndSend("/topic/comment-typing/" + status.getBookId(), status);
    }

    /**
     * ?볤? ?낅젰 醫낅즺 ?대깽??泥섎━
     * ?대씪?댁뼵?? /app/comment-typing/stop
     * 釉뚮줈?쒖틦?ㅽ듃: /topic/comment-typing/{bookId}
     */
    @MessageMapping("/comment-typing/stop")
    public void handleCommentTypingStop(TypingStatus status) {
        status.setTyping(false);
        log.debug("Comment typing stopped - Book: {}, User: {}", status.getBookId(), status.getUserNickname());
        messagingTemplate.convertAndSend("/topic/comment-typing/" + status.getBookId(), status);
    }
}
