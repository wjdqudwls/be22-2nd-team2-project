package com.team2.storyservice.query.book.mapper;

import com.team2.storyservice.query.book.dto.request.BookSearchRequest;
import com.team2.storyservice.query.book.dto.response.BookDetailDto;
import com.team2.storyservice.query.book.dto.response.BookDto;
import com.team2.storyservice.query.book.dto.response.SentenceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * ?뚯꽕 Query Mapper (寃?? ?꾪꽣留? ?섏씠吏?理쒖쟻??
 *
 * @author ?뺤쭊??
 */
@Mapper
public interface BookMapper {

    /**
     * ?뚯꽕 紐⑸줉 議고쉶 (?꾪꽣留?寃???섏씠吏?
     */
    List<BookDto> findBooks(@Param("request") BookSearchRequest request);

    /**
     * ?뚯꽕 ?꾩껜 媛쒖닔 議고쉶 (?섏씠吏뺤슜)
     */
    Long countBooks(@Param("request") BookSearchRequest request);

    /**
     * ?뚯꽕 ?곸꽭 議고쉶
     */
    BookDto findBookDetail(@Param("bookId") Long bookId);

    /**
     * ?뚯꽕 ?곸꽭 議고쉶 (酉곗뼱??- ?묒꽦???됰꽕???ы븿)
     */
    BookDetailDto findBookForViewer(@Param("bookId") Long bookId, @Param("userId") Long userId);

    /**
     * ?뚯꽕??臾몄옣 紐⑸줉 議고쉶
     */
    List<SentenceDto> findSentencesByBookId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    /**
     * ?뚯꽕??醫뗭븘????議고쉶
     */
    Integer countLikes(@Param("bookId") Long bookId);

    /**
     * ?뚯꽕???レ뼱????議고쉶
     */
    Integer countDislikes(@Param("bookId") Long bookId);

    /**
     * 湲곗〈 硫붿꽌??(?섏쐞 ?명솚??
     */
    List<BookDto> findAllBooks();

    /**
     * ?뱀젙 ?ъ슜?먭? ??臾몄옣 紐⑸줉 議고쉶 (?섏씠吏?
     */
    List<SentenceDto> findSentencesByWriterId(@Param("writerId") Long writerId, @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * ?뱀젙 ?ъ슜?먭? ??臾몄옣 ?꾩껜 媛쒖닔
     */
    Long countSentencesByWriterId(@Param("writerId") Long writerId);
}
