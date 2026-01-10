package com.team2.nextpage.command.reaction.controller;

import com.team2.nextpage.command.reaction.service.ReactionService;
import com.team2.nextpage.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 반응(댓글/투표) Command 컨트롤러
 *
 * @author 정병진
 */
@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    /**
     * 댓글 등록 API
     * POST /api/reactions/comments
     */
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<Long>> createComment(/* @RequestBody CommentRequest request */) {
        Long commentId = reactionService.addComment(/* request */);
        return ResponseEntity.ok(ApiResponse.success(commentId));
    }

    /**
     * 투표 API
     * POST /api/reactions/votes
     */
    @PostMapping("/votes")
    public ResponseEntity<ApiResponse<Boolean>> vote(/* @RequestBody VoteRequest request */) {
        Boolean result = reactionService.voteBook(/* request */);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
