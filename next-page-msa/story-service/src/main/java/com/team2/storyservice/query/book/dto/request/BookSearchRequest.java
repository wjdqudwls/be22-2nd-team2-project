package com.team2.storyservice.query.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ?뚯꽕 寃???붿껌 DTO (?섏씠吏? ?뺣젹, ?꾪꽣留?
 *
 * @author ?뺤쭊??
 */
@Getter
@Setter
@NoArgsConstructor
public class BookSearchRequest {

    // ?섏씠吏?
    private Integer page = 0; // ?섏씠吏 踰덊샇 (0遺???쒖옉)
    private Integer size = 10; // ?섏씠吏 ?ш린

    // ?뺣젹
    private String sortBy = "createdAt"; // ?뺣젹 湲곗? (createdAt, title)
    private String sortOrder = "DESC"; // ?뺣젹 諛⑺뼢 (ASC, DESC)

    // ?꾪꽣留?
    private String status; // ?곹깭 ?꾪꽣 (WRITING, COMPLETED)
    private String categoryId; // 移댄뀒怨좊━ ?꾪꽣
    private String keyword; // ?쒕ぉ 寃???ㅼ썙??
    private Long writerId; // ?묒꽦??ID ?꾪꽣

    /**
     * ?섏씠吏 ?ㅽ봽??怨꾩궛
     */
    public Integer getOffset() {
        return page * size;
    }
}
