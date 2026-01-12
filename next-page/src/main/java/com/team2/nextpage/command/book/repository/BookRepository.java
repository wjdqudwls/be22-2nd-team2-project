package com.team2.nextpage.command.book.repository;

import com.team2.nextpage.command.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 소설 Command Repository
 *
 * @author 정진호
 */
public interface BookRepository extends JpaRepository<Book, Long> {
}
