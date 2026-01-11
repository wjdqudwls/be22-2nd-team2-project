package com.team2.nextpage.query.book.service;

import com.team2.nextpage.query.book.dto.response.BookDto;
import com.team2.nextpage.query.book.mapper.BookMapper;
import com.team2.nextpage.common.error.BusinessException;
import com.team2.nextpage.common.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 소설 Query 서비스 (조회 전용)
 *
 * @author 정진호
 */
@Service
@Transactional(readOnly = true)
public class BookQueryService {

    private final BookMapper bookMapper;

    public BookQueryService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 소설 검색 및 목록 조회
     */
    public List<BookDto> searchBooks() {
        return bookMapper.findAllBooks();
    }

    /**
     * 소설 상세 보기 (뷰어)
     */
    public BookDto getBook(Long bookId) {
        BookDto book = bookMapper.findBookDetail(bookId);
        if (book == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }
        return book;
    }
}
