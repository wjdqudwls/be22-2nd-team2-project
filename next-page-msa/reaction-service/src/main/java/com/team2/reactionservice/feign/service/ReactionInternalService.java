package com.team2.reactionservice.feign.service;

import com.team2.commonmodule.feign.dto.MemberReactionStatsDto;
import com.team2.commonmodule.feign.dto.SentenceReactionInfoDto;
import com.team2.reactionservice.command.reaction.entity.VoteType;
import com.team2.reactionservice.command.reaction.repository.CommentRepository;
import com.team2.reactionservice.command.reaction.repository.SentenceVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ReactionInternalService
 *
 * @author 정병진
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionInternalService {

    private final CommentRepository commentRepository;
    private final SentenceVoteRepository sentenceVoteRepository;

    public MemberReactionStatsDto getMemberReactionStats(Long userId) {
        int writtenCommentCount = commentRepository.countByWriterId(userId);

        return MemberReactionStatsDto.builder()
                .writtenCommentCount(writtenCommentCount)
                .build();
    }

    public Map<Long, SentenceReactionInfoDto> getSentenceReactions(
            List<Long> sentenceIds, Long userId) {
        Map<Long, SentenceReactionInfoDto> statsMap = new HashMap<>();

        if (sentenceIds == null || sentenceIds.isEmpty()) {
            return statsMap;
        }

        // 1. Initialize map
        for (Long id : sentenceIds) {
            statsMap.put(id, new SentenceReactionInfoDto(id, 0L, 0L, null));
        }

        // 2. Count Votes
        List<Object[]> counts = sentenceVoteRepository.countVotesBySentenceIds(sentenceIds);
        for (Object[] row : counts) {
            Long sentenceId = (Long) row[0];
            VoteType type = (VoteType) row[1];
            Long count = (Long) row[2];

            SentenceReactionInfoDto dto = statsMap.get(sentenceId);
            if (dto != null) {
                if (type == VoteType.LIKE)
                    dto.setLikeCount(count);
                else if (type == VoteType.DISLIKE)
                    dto.setDislikeCount(count);
            }
        }

        // 3. User Vote Status
        if (userId != null) {
            List<Object[]> myVotes = sentenceVoteRepository.findMyVotesBySentenceIds(sentenceIds, userId);
            for (Object[] row : myVotes) {
                Long sentenceId = (Long) row[0];
                VoteType type = (VoteType) row[1];

                SentenceReactionInfoDto dto = statsMap.get(sentenceId);
                if (dto != null) {
                    dto.setMyVote(type.name());
                }
            }
        }

        return statsMap;
    }
}
