package com.team2.storyservice.query.book.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.team2.storyservice.command.book.controller.BookController;
// TODO: Cross-service HATEOAS links to reaction-service removed for MSA
// Will be implemented via API Gateway or removed

import com.team2.storyservice.query.book.dto.request.BookSearchRequest;
import com.team2.storyservice.query.book.dto.response.BookDetailDto;
import com.team2.storyservice.query.book.dto.response.BookDto;
import com.team2.storyservice.query.book.dto.response.BookPageResponse;
import com.team2.storyservice.query.book.dto.response.SentenceDto;
import com.team2.storyservice.query.book.dto.response.SentencePageResponse;
import com.team2.storyservice.query.book.service.BookQueryService;
import com.team2.commonmodule.response.ApiResponse;
import com.team2.commonmodule.util.SecurityUtil;
import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ?뚯꽕 Query 而⑦듃濡ㅻ윭
 *
 * @author ?뺤쭊??
 */
@Tag(name = "Book Queries", description = "?뚯꽕 議고쉶(Query) API")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class BookQueryController {

    private final BookQueryService bookQueryService;

    /**
     * ?뚯꽕 紐⑸줉 議고쉶 API (?섏씠吏??뺣젹/?꾪꽣留?
     * GET /api/books
     *
     * @param request 寃??議곌굔 (page, size, sortBy, sortOrder, status, categoryId,
     *                keyword)
     * @return ?섏씠吏뺣맂 ?뚯꽕 紐⑸줉
     */
    @Operation(summary = "?뚯꽕 紐⑸줉 議고쉶", description = "議곌굔???곕씪 ?뚯꽕 紐⑸줉???섏씠吏뺥븯??議고쉶?⑸땲??")
    @GetMapping
    public ResponseEntity<ApiResponse<BookPageResponse>> list(@ModelAttribute BookSearchRequest request) {
        BookPageResponse result = bookQueryService.searchBooks(request);
        if (result.getContent() != null) {
            for (BookDto book : result.getContent()) {
                book.add(linkTo(methodOn(BookQueryController.class).detail(book.getBookId())).withSelfRel());
                book.add(linkTo(methodOn(BookQueryController.class).view(book.getBookId())).withRel("view"));
            }
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * ?뚯꽕 ?곸꽭 議고쉶 API
     * GET /api/books/{bookId}
     *
     * @param bookId ?뚯꽕 ID
     * @return ?뚯꽕 湲곕낯 ?뺣낫
     */
    @Operation(summary = "?뚯꽕 ?곸꽭 議고쉶", description = "?뚯꽕??湲곕낯 ?뺣낫瑜?議고쉶?⑸땲??")
    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookDto>> detail(@PathVariable Long bookId) {
        BookDto book = bookQueryService.getBook(bookId);

        // HATEOAS Links (cross-service links removed for MSA)
        book.add(linkTo(methodOn(BookQueryController.class).detail(bookId)).withSelfRel());
        book.add(linkTo(methodOn(BookQueryController.class).view(bookId)).withRel("view"));
        // TODO: Add cross-service links via API Gateway

        return ResponseEntity.ok().body(ApiResponse.success(book));
    }

    /**
     * ?뚯꽕 酉곗뼱 紐⑤뱶 議고쉶 API (臾몄옣 紐⑸줉 ?ы븿)
     * GET /api/books/{bookId}/view
     *
     * @param bookId ?뚯꽕 ID
     * @return ?뚯꽕 ?곸꽭 ?뺣낫 (臾몄옣 紐⑸줉, ?ы몴 ?듦퀎 ?ы븿)
     */
    @Operation(summary = "?뚯꽕 酉곗뼱 議고쉶", description = "?뚯꽕???꾩껜 臾몄옣???ы븿?섏뿬 ?쎄린 紐⑤뱶濡?議고쉶?⑸땲??")
    @GetMapping("/{bookId}/view")
    public ResponseEntity<ApiResponse<BookDetailDto>> view(@PathVariable Long bookId) {
        BookDetailDto bookDetail = bookQueryService.getBookForViewer(bookId);

        // HATEOAS Links (cross-service links removed for MSA)
        bookDetail.add(linkTo(methodOn(BookQueryController.class).view(bookId)).withSelfRel());
        bookDetail.add(linkTo(methodOn(BookQueryController.class).detail(bookId)).withRel("detail"));
        // TODO: Add cross-service links via API Gateway

        if ("IN_PROGRESS".equals(bookDetail.getStatus())) {
            bookDetail.add(linkTo(methodOn(BookController.class).append(bookId, null)).withRel("append-sentence"));
        }

        // TODO: Add sentence vote links via API Gateway to reaction-service
        // Removed cross-service HATEOAS links for MSA architecture

        return ResponseEntity.ok().body(ApiResponse.success(bookDetail));
    }

    /**
     * ?닿? ??臾몄옣 紐⑸줉 議고쉶 API (?섏씠吏?
     * GET /api/books/mysentences
     */
    @Operation(summary = "?닿? ??臾몄옣 議고쉶", description = "?꾩옱 濡쒓렇?명븳 ?ъ슜?먭? ?묒꽦??臾몄옣 紐⑸줉??議고쉶?⑸땲??")
    @GetMapping("/mysentences")
    public ResponseEntity<ApiResponse<SentencePageResponse>> getMySentences(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED);
        }

        SentencePageResponse response = bookQueryService.getSentencesByUser(userId, page, size);
        // TODO: Add sentence vote links via API Gateway to reaction-service
        // Removed cross-service HATEOAS links for MSA architecture
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }
}
