package com.team2.storyservice.query.book.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

/**
 * 臾몄옣(Sentence) 議고쉶??DTO
 *
 * @author ?뺤쭊??
 */
@Getter
@Setter
@NoArgsConstructor
public class SentenceDto extends RepresentationModel<SentenceDto> {
    private Long sentenceId;
    private Integer sequenceNo; // 臾몄옣 ?쒖꽌
    private String content; // 臾몄옣 ?댁슜
    private Long writerId; // ?묒꽦??ID
    private String writerNicknm; // ?묒꽦???됰꽕??
    private LocalDateTime createdAt; // ?묒꽦 ?쒓컙
    private Integer likeCount; // 醫뗭븘????
    private Integer dislikeCount; // ?レ뼱????
    private String myVote; // ???ы몴 ?곹깭 (LIKE/DISLIKE/null)
    private Long bookId; // ?뚯꽕 ID
    private String bookTitle; // ?뚯꽕 ?쒕ぉ
}
