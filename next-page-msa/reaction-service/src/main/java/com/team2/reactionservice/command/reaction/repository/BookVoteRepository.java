package com.team2.reactionservice.command.reaction.repository;

import com.team2.reactionservice.command.reaction.entity.BookVote;
import com.team2.reactionservice.command.reaction.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 소설 투표(BookVote) Command Repository
 *
 * @author 정병진
 */
public interface BookVoteRepository extends JpaRepository<BookVote, Long> {

    /**
     * 소설과 투표자로 투표 조회 (중복 투표 확인용)
     *
     * @param bookId  소설 ID
     * @param voterId 투표자 ID
     * @return 투표 정보 (Optional)
     */
    Optional<BookVote> findByBookIdAndVoterId(Long bookId, Long voterId);

    /**
     * 소설과 투표자로 투표 존재 여부 확인
     *
     * @param bookId  소설 ID
     * @param voterId 투표자 ID
     * @return 존재 여부
     */
    boolean existsByBookIdAndVoterId(Long bookId, Long voterId);

    /**
     * 소설의 특정 투표 유형 개수 조회
     *
     * @param bookId   소설 ID
     * @param voteType 투표 유형 (LIKE/DISLIKE)
     * @return 투표 수
     */
    long countByBookIdAndVoteType(Long bookId, VoteType voteType);

    /**
     * 소설과 투표자로 투표 삭제
     *
     * @param bookId  소설 ID
     * @param voterId 투표자 ID
     */
    void deleteByBookIdAndVoterId(Long bookId, Long voterId);
}
