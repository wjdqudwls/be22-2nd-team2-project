package com.team2.nextpage.query.reaction.service;

import com.team2.nextpage.query.reaction.dto.response.CommentDto;
import com.team2.nextpage.query.reaction.mapper.ReactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 반응 Query 서비스 (댓글 목록 조회 등)
 *
 * @author 정병진
 */
@Service
@Transactional(readOnly = true)
public class ReactionQueryService {

    private final ReactionMapper reactionMapper;

    public ReactionQueryService(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    /**
     * 댓글 목록 조회
     */
    public List<CommentDto> getComments(Long bookId) {
        return null; // impl
    }
}
