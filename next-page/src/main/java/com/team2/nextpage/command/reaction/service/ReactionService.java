package com.team2.nextpage.command.reaction.service;

import com.team2.nextpage.command.reaction.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 반응(댓글/투표) Command 서비스
 *
 * @author 정병진
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 작성
     *
     * [Hint]
     * 1. CommentRequest(content, bookId)를 인자로 받습니다.
     * 2. Comment Entity를 생성하고 저장합니다 (ModelMapper.map() 또는 Comment.builder() 활용).
     * 3. 저장된 Comment ID를 반환합니다.
     */
    public Long addComment(/* CommentRequest request */) {
        // TODO: 정병진 구현 필요
        return null;
    }

    /**
     * 소설 좋아요 투표
     *
     * [Hint]
     * 1. VoteRequest(bookId, voteType)를 인자로 받습니다.
     * 2. 이미 투표했는지 중복 확인을 수행합니다.
     * 3. BookVote Entity를 저장합니다.
     * 4. 투표 결과를 반환합니다 (true: 투표 성공, false: 취소 등).
     */
    public Boolean voteBook(/* VoteRequest request */) {
        // TODO: 정병진 구현 필요
        return null;
    }
}
