package com.team2.nextpage.command.book.repository;

import com.team2.nextpage.command.book.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 문장(Sentence) Command Repository
 *
 * @author 정진호
 */
public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
