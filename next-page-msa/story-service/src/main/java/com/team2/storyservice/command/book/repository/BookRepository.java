package com.team2.storyservice.command.book.repository;

import com.team2.storyservice.command.book.entity.Book;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * ?뚯꽕 Command Repository
 *
 * @author ?뺤쭊??
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * 臾몄옣 ?묒꽦 ???숈떆???쒖뼱瑜??꾪빐 鍮꾧?????X-Lock)??嫄멸퀬 議고쉶?⑸땲??
     * ?ㅻⅨ ?몃옖??뀡?????뚯꽕???묎렐?섏? 紐삵븯?꾨줉 留됱뒿?덈떎.
     *
     * @param bookId 議고쉶???뚯꽕 ID
     * @return ?뚯꽕 ?뷀떚??(Lock 嫄몃┝)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "3000") }) // 3珥???꾩븘??
    @Query("SELECT b FROM Book b WHERE b.bookId = :bookId")
    Optional<Book> findByIdForUpdate(@Param("bookId") Long bookId);
}
