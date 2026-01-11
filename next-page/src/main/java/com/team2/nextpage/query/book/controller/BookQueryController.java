package com.team2.nextpage.query.book.controller;

import com.team2.nextpage.query.book.dto.response.BookDto;
import com.team2.nextpage.query.book.service.BookQueryService;
import com.team2.nextpage.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 소설 Query 컨트롤러
 *
 * @author 정진호
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookQueryController {

    private final BookQueryService bookQueryService;

    /**
     * 소설 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(bookQueryService.searchBooks()));
    }

    /**
     * 소설 상세 조회 API
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookDto>> detail(@PathVariable Long bookId) {
        return ResponseEntity.ok(ApiResponse.success(bookQueryService.getBook(bookId)));
    }

    /**
     * 완결 소설 책 뷰어 모드 조회 API
     */
    @GetMapping("/{bookId}/view")
    public ResponseEntity<ApiResponse<BookDto>> view(@PathVariable Long bookId) {
        // 뷰어 모드도 현재는 상세 조회와 동일한 데이터를 반환한다고 가정
        return ResponseEntity.ok(ApiResponse.success(bookQueryService.getBook(bookId)));
    }
}
