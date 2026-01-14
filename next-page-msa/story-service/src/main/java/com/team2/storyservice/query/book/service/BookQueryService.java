package com.team2.storyservice.query.book.service;

import com.team2.storyservice.query.book.dto.request.BookSearchRequest;
import com.team2.storyservice.query.book.dto.response.BookDetailDto;
import com.team2.storyservice.query.book.dto.response.BookDto;
import com.team2.storyservice.query.book.dto.response.BookPageResponse;
import com.team2.storyservice.query.book.dto.response.SentenceDto;
import com.team2.storyservice.query.book.dto.response.SentencePageResponse;
import com.team2.storyservice.query.book.mapper.BookMapper;
import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * ?뚯꽕 Query ?쒕퉬??(議고쉶 ?꾩슜)
 *
 * @author ?뺤쭊??
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookQueryService {

    private final BookMapper bookMapper;

    /**
     * ?뚯꽕 寃??諛?紐⑸줉 議고쉶 (?섏씠吏??뺣젹/?꾪꽣留?
     *
     * @param request 寃??議곌굔 (?섏씠吏, ?뺣젹, ?꾪꽣)
     * @return ?섏씠吏뺣맂 ?뚯꽕 紐⑸줉
     */
    public BookPageResponse searchBooks(BookSearchRequest request) {
        // 寃??議곌굔??留욌뒗 ?뚯꽕 紐⑸줉 議고쉶
        List<BookDto> books = bookMapper.findBooks(request);

        // ?꾩껜 媛쒖닔 議고쉶 (?섏씠吏??뺣낫??
        Long totalElements = bookMapper.countBooks(request);

        return new BookPageResponse(books, request.getPage(), request.getSize(), totalElements);
    }

    /**
     * ?뚯꽕 紐⑸줉 議고쉶 (湲곕낯 - ?섏쐞 ?명솚??
     *
     * @return ?꾩껜 ?뚯꽕 紐⑸줉
     * @deprecated searchBooks(BookSearchRequest) ?ъ슜 沅뚯옣
     */
    @Deprecated
    public List<BookDto> searchBooks() {
        return bookMapper.findAllBooks();
    }

    /**
     * ?뚯꽕 ?곸꽭 蹂닿린 (湲곕낯 ?뺣낫留?
     *
     * @param bookId ?뚯꽕 ID
     * @return ?뚯꽕 湲곕낯 ?뺣낫
     * @throws BusinessException ?뚯꽕??李얠쓣 ???녿뒗 寃쎌슦
     */
    public BookDto getBook(Long bookId) {
        BookDto book = bookMapper.findBookDetail(bookId);
        if (book == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }
        return book;
    }

    /**
     * ?뚯꽕 酉곗뼱 紐⑤뱶 議고쉶 (臾몄옣 紐⑸줉 諛??ы몴 ?듦퀎 ?ы븿)
     *
     * @param bookId ?뚯꽕 ID
     * @return ?뚯꽕 ?곸꽭 ?뺣낫 (臾몄옣 紐⑸줉, ?ы몴 ?듦퀎 ?ы븿)
     * @throws BusinessException ?뚯꽕??李얠쓣 ???녿뒗 寃쎌슦
     */
    public BookDetailDto getBookForViewer(Long bookId) {
        Long userId = com.team2.commonmodule.util.SecurityUtil.getCurrentUserId();

        // 1. ?뚯꽕 湲곕낯 ?뺣낫 議고쉶
        BookDetailDto book = bookMapper.findBookForViewer(bookId, userId);
        if (book == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }

        // 2. 臾몄옣 紐⑸줉 議고쉶
        List<SentenceDto> sentences = bookMapper.findSentencesByBookId(bookId, userId);
        book.setSentences(sentences);

        // 3. ?ы몴 ?듦퀎 議고쉶
        book.setLikeCount(bookMapper.countLikes(bookId));
        book.setDislikeCount(bookMapper.countDislikes(bookId));

        return book;
    }

    /**
     * ?뱀젙 ?ъ슜?먭? ?묒꽦??臾몄옣 紐⑸줉 議고쉶 (?섏씠吏?
     */
    public SentencePageResponse getSentencesByUser(Long userId, int page, int size) {
        int offset = page * size;
        List<SentenceDto> sentences = bookMapper.findSentencesByWriterId(userId, offset, size);
        Long totalElements = bookMapper.countSentencesByWriterId(userId);

        return new SentencePageResponse(sentences, page, size, totalElements);
    }
}
