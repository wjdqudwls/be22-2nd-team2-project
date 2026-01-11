package com.team2.nextpage.query.book.dto.response;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 소설 조회용 DTO
 *
 * @author 정진호
 */
@Getter
public class BookDto {
    private Long bookId;
    private Long writerId;
    private String categoryId;
    private String title;
    private String status;
    private Integer currentSequence;
    private Integer maxSequence;
    private LocalDateTime createdAt;
}
