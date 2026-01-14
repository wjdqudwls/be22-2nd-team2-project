package com.team2.storyservice.query.book.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * ?뚯꽕 酉곗뼱???곸꽭 DTO (臾몄옣 紐⑸줉 ?ы븿)
 *
 * @author ?뺤쭊??
 */
@Getter
@Setter
@NoArgsConstructor
public class BookDetailDto extends RepresentationModel<BookDetailDto> {
    // 湲곕낯 ?뺣낫
    private Long bookId;
    private Long writerId;
    private String writerNicknm; // ?뚯꽕 ?앹꽦???됰꽕??
    private String categoryId;
    private String title;
    private String status;
    private Integer currentSequence;
    private Integer maxSequence;
    private Long lastWriterUserId;
    private LocalDateTime createdAt;

    // 臾몄옣 紐⑸줉
    private List<SentenceDto> sentences;

    // ?ы몴 ?듦퀎
    private Integer likeCount; // 醫뗭븘????
    private Integer dislikeCount; // ?レ뼱????
    private String myVote; // ???ы몴 ?곹깭 (LIKE/DISLIKE/null)
}
