package com.team2.nextpage.command.reaction.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 소설 투표(BookVote) 엔티티
 * 소설에 대한 좋아요/싫어요 투표를 관리합니다.
 * 1인 1투표 제한 (book_id, voter_id 복합 유니크 제약)
 *
 * @author 정병진
 */
@Entity
@Getter
@Table(name = "book_votes", uniqueConstraints = @UniqueConstraint(name = "uk_book_voter", columnNames = { "book_id",
        "voter_id" }))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "voter_id", nullable = false)
    private Long voterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false, length = 10)
    private VoteType voteType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private BookVote(Long bookId, Long voterId, VoteType voteType) {
        this.bookId = bookId;
        this.voterId = voterId;
        this.voteType = voteType;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 투표 유형 변경 (좋아요 <-> 싫어요 전환)
     *
     * @param newVoteType 변경할 투표 유형
     */
    public void changeVoteType(VoteType newVoteType) {
        this.voteType = newVoteType;
    }

    /**
     * 좋아요 투표인지 확인
     *
     * @return 좋아요이면 true
     */
    public boolean isLike() {
        return this.voteType == VoteType.LIKE;
    }

    /**
     * 싫어요 투표인지 확인
     *
     * @return 싫어요이면 true
     */
    public boolean isDislike() {
        return this.voteType == VoteType.DISLIKE;
    }
}
