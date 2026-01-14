package com.team2.storyservice.command.book.repository;

import com.team2.storyservice.command.book.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 臾몄옣(Sentence) Command Repository
 *
 * @author ?뺤쭊??
 */
public interface SentenceRepository extends JpaRepository<Sentence, Long> {

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE Sentence s SET s.sequenceNo = s.sequenceNo - 1 WHERE s.book.bookId = :bookId AND s.sequenceNo > :sequenceNo")
    void decreaseSequenceAfter(@org.springframework.data.repository.query.Param("bookId") Long bookId,
            @org.springframework.data.repository.query.Param("sequenceNo") Integer sequenceNo);

    java.util.Optional<Sentence> findByBookAndSequenceNo(com.team2.storyservice.command.book.entity.Book book,
            Integer sequenceNo);
}
