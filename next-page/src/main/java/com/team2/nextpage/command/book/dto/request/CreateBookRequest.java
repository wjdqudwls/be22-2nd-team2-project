package com.team2.nextpage.command.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소설 생성 요청 DTO
 *
 * @author 최현지
 */
@Getter
@NoArgsConstructor
public class CreateBookRequest {
    private String title;
    private String categoryId;
    private Integer maxSequence;
    private String firstSentence;
}
