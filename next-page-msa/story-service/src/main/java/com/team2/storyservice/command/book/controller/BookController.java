package com.team2.storyservice.command.book.controller;

import com.team2.storyservice.command.book.dto.request.CreateBookRequest;
import com.team2.storyservice.command.book.dto.request.SentenceAppendRequest;
import com.team2.storyservice.command.book.service.BookService;
import com.team2.commonmodule.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.team2.storyservice.command.book.dto.request.UpdateBookRequest;
import com.team2.storyservice.command.book.dto.request.UpdateSentenceRequest;

import jakarta.validation.Valid;

/**
 * ?뚯꽕 Command 而⑦듃濡ㅻ윭
 *
 * @author ?뺤쭊??
 */
@Tag(name = "Book Commands", description = "?뚯꽕 ?앹꽦 諛?愿由?Command) API")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * ?뚯꽕 ?앹꽦 API
     * POST /api/books
     */
    @Operation(summary = "?뚯꽕 ?앹꽦", description = "?덈줈??由대젅???뚯꽕???앹꽦?⑸땲??")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "400", description = "?섎せ???붿껌")
    })
    @PostMapping
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Long>> create(
            @RequestBody @Valid CreateBookRequest request) {
        Long writerId = SecurityUtil.getCurrentUserId();
        Long bookId = bookService.createBook(writerId, request);
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success(bookId));
    }

    /**
     * 臾몄옣 ?댁뼱?곌린 API
     * POST /api/books/{bookId}/sentences
     */
    @Operation(summary = "臾몄옣 ?댁뼱?곌린", description = "吏꾪뻾 以묒씤 ?뚯꽕???덈줈??臾몄옣??異붽??⑸땲??")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "400", description = "?섎せ???붿껌 (湲?먯닔 ?쒗븳 ??"),
            @ApiResponse(responseCode = "403", description = "?묒꽦 沅뚰븳 ?놁쓬 (?곗냽 ?묒꽦 遺덇? ??")
    })
    @PostMapping("/{bookId}/sentences")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Long>> append(@PathVariable Long bookId,
            @RequestBody @Valid SentenceAppendRequest request) {
        Long writerId = SecurityUtil.getCurrentUserId();
        Long sentenceId = bookService.appendSentence(bookId, writerId, request);
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success(sentenceId));
    }

    /**
     * ?뚯꽕 ?섎룞 ?꾧껐 API (?묒꽦???꾩슜)
     * POST /api/books/{bookId}/complete
     */
    @Operation(summary = "?뚯꽕 ?꾧껐", description = "吏꾪뻾 以묒씤 ?뚯꽕???묒꽦?먭? ?섎룞?쇰줈 ?꾧껐?쒗궢?덈떎.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "403", description = "沅뚰븳 ?놁쓬 (?묒꽦???꾨떂)"),
            @ApiResponse(responseCode = "400", description = "?대? ?꾧껐???뚯꽕")
    })
    @PostMapping("/{bookId}/complete")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Void>> complete(@PathVariable Long bookId) {
        Long writerId = SecurityUtil.getCurrentUserId();
        bookService.completeBook(bookId, writerId);
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success());
    }

    /**
     * ?뚯꽕 ?쒕ぉ ?섏젙 API (?묒꽦???먮뒗 愿由ъ옄)
     * PATCH /api/books/{bookId}
     */
    @Operation(summary = "?뚯꽕 ?쒕ぉ ?섏젙", description = "?뚯꽕???쒕ぉ???섏젙?⑸땲?? ?묒꽦???먮뒗 愿由ъ옄留?媛?ν빀?덈떎.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "403", description = "沅뚰븳 ?놁쓬"),
            @ApiResponse(responseCode = "404", description = "議댁옱?섏? ?딅뒗 ?뚯꽕")
    })
    @PatchMapping("/{bookId}")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Void>> updateBookTitle(
            @PathVariable Long bookId,
            @RequestBody @Valid UpdateBookRequest request) {
        Long requesterId = SecurityUtil.getCurrentUserId();
        bookService.updateBookTitle(bookId, requesterId, request.getTitle());
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success());
    }

    /**
     * 臾몄옣 ?댁슜 ?섏젙 API (?묒꽦???먮뒗 愿由ъ옄)
     * PATCH /api/books/{bookId}/sentences/{sentenceId}
     */
    @Operation(summary = "臾몄옣 ?섏젙", description = "臾몄옣???댁슜???섏젙?⑸땲?? ?묒꽦???먮뒗 愿由ъ옄留?媛?ν빀?덈떎.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "403", description = "沅뚰븳 ?놁쓬"),
            @ApiResponse(responseCode = "404", description = "議댁옱?섏? ?딅뒗 臾몄옣")
    })
    @PatchMapping("/{bookId}/sentences/{sentenceId}")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Void>> updateSentence(
            @PathVariable Long bookId,
            @PathVariable Long sentenceId,
            @RequestBody @Valid UpdateSentenceRequest request) {
        Long requesterId = SecurityUtil.getCurrentUserId();
        bookService.updateSentence(bookId, sentenceId, requesterId, request.getContent());
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success());
    }

    /**
     * ?뚯꽕 ??젣 API (?묒꽦???먮뒗 愿由ъ옄)
     * DELETE /api/books/{bookId}
     */
    @Operation(summary = "?뚯꽕 ??젣", description = "?뚯꽕????젣?⑸땲?? ?묒꽦???먮뒗 愿由ъ옄留?媛?ν빀?덈떎.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "403", description = "沅뚰븳 ?놁쓬"),
            @ApiResponse(responseCode = "404", description = "議댁옱?섏? ?딅뒗 ?뚯꽕")
    })
    @DeleteMapping("/{bookId}")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Void>> deleteBook(@PathVariable Long bookId) {
        Long requesterId = SecurityUtil.getCurrentUserId();
        bookService.deleteBook(bookId, requesterId);
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success());
    }

    /**
     * 臾몄옣 ??젣 API (?묒꽦???먮뒗 愿由ъ옄)
     * DELETE /api/books/{bookId}/sentences/{sentenceId}
     */
    @Operation(summary = "臾몄옣 ??젣", description = "臾몄옣????젣?⑸땲?? ??젣 ???쒖꽌???먮룞 議곗젙?⑸땲?? ?묒꽦???먮뒗 愿由ъ옄留?媛?ν빀?덈떎.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "?깃났"),
            @ApiResponse(responseCode = "403", description = "沅뚰븳 ?놁쓬"),
            @ApiResponse(responseCode = "404", description = "議댁옱?섏? ?딅뒗 臾몄옣")
    })
    @DeleteMapping("/{bookId}/sentences/{sentenceId}")
    public ResponseEntity<com.team2.commonmodule.response.ApiResponse<Void>> deleteSentence(
            @PathVariable Long bookId,
            @PathVariable Long sentenceId) {
        Long requesterId = SecurityUtil.getCurrentUserId();
        bookService.deleteSentence(bookId, sentenceId, requesterId);
        return ResponseEntity.ok(com.team2.commonmodule.response.ApiResponse.success());
    }
}
