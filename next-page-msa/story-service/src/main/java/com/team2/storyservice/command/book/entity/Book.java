package com.team2.storyservice.command.book.entity;

import com.team2.commonmodule.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;

/**
 * ?뚯꽕(Book) ?좉렇由ш굅??猷⑦듃
 *
 * @author ?뺤쭊??
 */
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(name = "writer_id")
    private Long writerId;

    @Column(name = "category_id")
    private String categoryId;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'WRITING'")
    private BookStatus status; // WRITING, COMPLETED

    @Column(name = "current_sequence")
    private Integer currentSequence;

    @Column(name = "max_sequence")
    private Integer maxSequence;

    @Column(name = "last_writer_user_id")
    private Long lastWriterUserId;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Sentence> sentences = new ArrayList<>();

    // =================================================================
    // ?룢截?Domain Logic (Story Aggregate State Management)
    // Assigned to: ?뺤쭊??(Core & Architecture)
    // =================================================================

    /**
     * 臾몄옣???묒꽦?섍린 ?꾩뿉 ?꾨찓??洹쒖튃??寃利앺빀?덈떎.
     * 1. ?뚯꽕??WRITING ?곹깭?몄?
     * 2. 吏곸쟾 ?묒꽦?먭? 蹂몄씤???꾨땶吏 (?곗냽 ?묒꽦 湲덉?)
     *
     * @param writerId ?묒꽦 ?쒕룄?섎뒗 ?ъ슜??ID
     * @throws BusinessException 洹쒖튃 ?꾨컲 ??
     */
    public void validateWritingPossible(Long writerId) {
        validateWritingPossible(writerId, false);
    }

    /**
     * 臾몄옣???묒꽦?섍린 ?꾩뿉 ?꾨찓??洹쒖튃??寃利앺빀?덈떎. (愿由ъ옄 紐⑤뱶 吏??
     * 1. ?뚯꽕??WRITING ?곹깭?몄?
     * 2. 吏곸쟾 ?묒꽦?먭? 蹂몄씤???꾨땶吏 (?곗냽 ?묒꽦 湲덉?) - 愿由ъ옄???덉쇅
     *
     * @param writerId ?묒꽦 ?쒕룄?섎뒗 ?ъ슜??ID
     * @param isAdmin  愿由ъ옄 ?щ? (true: ?곗냽 ?묒꽦 ?쒗븳 ?고쉶)
     * @throws BusinessException 洹쒖튃 ?꾨컲 ??
     */
    public void validateWritingPossible(Long writerId, boolean isAdmin) {
        if (this.status != BookStatus.WRITING) {
            throw new BusinessException(ErrorCode.ALREADY_COMPLETED);
        }
        // 愿由ъ옄媛 ?꾨땶 寃쎌슦?먮쭔 ?곗냽 ?묒꽦 ?쒗븳 ?곸슜
        if (!isAdmin && writerId.equals(this.lastWriterUserId)) {
            throw new BusinessException(ErrorCode.CONSECUTIVE_WRITING_NOT_ALLOWED);
        }
    }

    /**
     * 臾몄옣 ?묒꽦???꾨즺?????뚯꽕???곹깭(?쒖꽌, 留덉?留??묒꽦??瑜??낅뜲?댄듃?⑸땲??
     * ?먰븳 理쒕? ?쒗?ㅼ뿉 ?꾨떖?섎㈃ ?뚯꽕???꾧껐 泥섎━?⑸땲??
     *
     * @param writerId ?묒꽦 ?꾨즺???ъ슜??ID
     */
    public void updateStateAfterWriting(Long writerId) {
        this.lastWriterUserId = writerId;
        this.currentSequence++;

        // ?꾧껐 議곌굔 泥댄겕
        if (this.currentSequence > this.maxSequence) {
            completeStory();
        }
    }

    /**
     * 臾몄옣 ??젣 ???쒗?ㅻ? 媛먯냼?쒗궢?덈떎.
     * lastWriterUserId???몃??먯꽌 泥섎━ ???ㅼ젙?댁빞 ?⑸땲??
     */
    public void decrementSequence() {
        if (this.currentSequence > 1) {
            this.currentSequence--;
        }
    }

    public void updateLastWriterUserId(Long writerId) {
        this.lastWriterUserId = writerId;
    }

    /**
     * ?뚯꽕???꾧껐 ?곹깭濡?蹂寃쏀빀?덈떎. (?대? 濡쒖쭅??
     */
    private void completeStory() {
        this.status = BookStatus.COMPLETED;
    }

    /**
     * ?묒꽦?먯뿉 ?섑빐 ?뚯꽕??媛뺤젣濡??꾧껐?⑸땲??
     * 沅뚰븳 泥댄겕: ?붿껌?먭? ?뚯꽕 ?앹꽦??writerId)?ъ빞 ?⑸땲??
     *
     * @param requesterId ?붿껌??ID
     */
    public void completeManually(Long requesterId) {
        if (!this.writerId.equals(requesterId)) {
            throw new BusinessException(ErrorCode.NOT_BOOK_OWNER);
        }
        if (this.status == BookStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.ALREADY_COMPLETED);
        }
        this.status = BookStatus.COMPLETED;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    /**
     * ?뚯꽕 ?앹꽦 ??珥덇린??(?⑺넗由?硫붿꽌?쒕굹 ?앹꽦?먯뿉???몄텧)
     */
    public void init(Long writerId, String categoryId, String title, Integer maxSequence) {
        this.writerId = writerId;
        this.categoryId = categoryId;
        this.title = title;
        this.maxSequence = maxSequence;
        this.status = BookStatus.WRITING;
        this.currentSequence = 1;
        this.lastWriterUserId = null; // ?꾩쭅 ?묒꽦???놁쓬 (泥?臾몄옣? 蹂꾨룄 濡쒖쭅 ?곕쫫 or 泥?臾몄옣 ?묒꽦??媛깆떊)
    }
}
