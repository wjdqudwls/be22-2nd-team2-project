package com.team2.reactionservice.command.reaction.repository;

import com.team2.reactionservice.command.reaction.entity.SentenceVote;
import com.team2.reactionservice.command.reaction.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 문장 투표(SentenceVote) Command Repository
 *
 * @author 정병진
 */
public interface SentenceVoteRepository extends JpaRepository<SentenceVote, Long> {

    /**
     * 문장과 투표자로 투표 조회 (중복 투표 확인용)
     *
     * @param sentenceId 문장 ID
     * @param voterId    투표자 ID
     * @return 투표 정보 (Optional)
     */
    Optional<SentenceVote> findBySentenceIdAndVoterId(Long sentenceId, Long voterId);

    /**
     * 문장과 투표자로 투표 존재 여부 확인
     *
     * @param sentenceId 문장 ID
     * @param voterId    투표자 ID
     * @return 존재 여부
     */
    boolean existsBySentenceIdAndVoterId(Long sentenceId, Long voterId);

    /**
     * 문장의 특정 투표 유형 개수 조회
     *
     * @param sentenceId 문장 ID
     * @param voteType   투표 유형 (LIKE/DISLIKE)
     * @return 투표 수
     */
    long countBySentenceIdAndVoteType(Long sentenceId, VoteType voteType);

    /**
     * 문장과 투표자로 투표 삭제
     *
     * @param sentenceId 문장 ID
     * @param voterId    투표자 ID
     */
    void deleteBySentenceIdAndVoterId(Long sentenceId, Long voterId);
}
