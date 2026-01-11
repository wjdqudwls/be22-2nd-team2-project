package com.team2.nextpage.command.book.repository;

import com.team2.nextpage.command.book.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
