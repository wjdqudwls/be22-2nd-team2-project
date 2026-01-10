package com.team2.nextpage.query.book.controller;

import com.team2.nextpage.query.book.dto.response.BookDto;
import com.team2.nextpage.query.book.service.BookQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 소설 Query 컨트롤러
 *
 * @author 최현지
 */
@RestController
@RequestMapping("/api/books")
public class BookQueryController {

    private final BookQueryService bookQueryService;

    public BookQueryController(BookQueryService bookQueryService) {
        this.bookQueryService = bookQueryService;
    }

    /**
     * 소설 목록 조회 API
     */
    @GetMapping
    public List<BookDto> list() {
        return null; // impl
    }

    /**
     * 소설 상세 조회 API
     */
    @GetMapping("/{bookId}")
    public BookDto detail(@PathVariable Long bookId) {
        return null; // impl
    }
}
