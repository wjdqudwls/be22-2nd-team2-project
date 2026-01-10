package com.team2.nextpage.query.book.mapper;

import com.team2.nextpage.query.book.dto.response.BookDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 소설 Query Mapper (검색, 필터링, 페이징 최적화)
 *
 * @author 최현지
 */
@Mapper
public interface BookMapper {

    /**
     * 소설 목록 조회 (필터링/검색)
     */
    List<BookDto> findAllBooks();

    /**
     * 소설 상세 조회
     */
    BookDto findBookDetail(Long bookId);
}
