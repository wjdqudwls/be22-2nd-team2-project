package com.team2.storyservice.query.book.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * ?뚯꽕 紐⑸줉 ?섏씠吏??묐떟 DTO
 *
 * @author ?뺤쭊??
 */
@Getter
@Setter
@NoArgsConstructor
public class BookPageResponse {
    private List<BookDto> content; // ?뚯꽕 紐⑸줉
    private Integer page; // ?꾩옱 ?섏씠吏 踰덊샇
    private Integer size; // ?섏씠吏 ?ш린
    private Long totalElements; // ?꾩껜 ?붿냼 ??
    private Integer totalPages; // ?꾩껜 ?섏씠吏 ??
    private Boolean hasNext; // ?ㅼ쓬 ?섏씠吏 議댁옱 ?щ?
    private Boolean hasPrevious; // ?댁쟾 ?섏씠吏 議댁옱 ?щ?

    public BookPageResponse(List<BookDto> content, Integer page, Integer size, Long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages - 1;
        this.hasPrevious = page > 0;
    }
}
