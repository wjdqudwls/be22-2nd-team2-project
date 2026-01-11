package com.team2.nextpage.command.book.service;

import com.team2.nextpage.command.book.dto.request.CreateBookRequest;
import com.team2.nextpage.command.book.dto.request.SentenceAppendRequest;
import com.team2.nextpage.command.book.entity.Book;
import com.team2.nextpage.command.book.entity.Sentence;
import com.team2.nextpage.command.book.repository.BookRepository;
import com.team2.nextpage.command.book.repository.SentenceRepository;
import com.team2.nextpage.common.error.BusinessException;
import com.team2.nextpage.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 소설 Command 서비스 (생성, 문장 작성)
 *
 * @author 정진호
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final SentenceRepository sentenceRepository;

    /**
     * 소설 방 생성
     */
    public Long createBook(Long writerId, CreateBookRequest request) {
        // 1. Book 생성
        Book book = Book.builder()
                .writerId(writerId)
                .categoryId(request.getCategoryId())
                .title(request.getTitle())
                .maxSequence(request.getMaxSequence())
                .status(com.team2.nextpage.command.book.entity.BookStatus.WRITING)
                .currentSequence(1) // 1번 문장부터 시작
                .build();

        Book savedBook = bookRepository.save(book);

        // 2. 첫 문장 자동 등록
        Sentence firstSentence = Sentence.builder()
                .book(savedBook)
                .writerId(writerId)
                .content(request.getFirstSentence())
                .sequenceNo(1)
                .build();

        sentenceRepository.save(firstSentence);

        // 3. 상태 업데이트 (1번 문장 작성 완료 처리 -> 다음은 2번)
        savedBook.updateStateAfterWriting(writerId);

        return savedBook.getBookId();
    }

    /**
     * 문장 이어 쓰기
     */
    public Long appendSentence(Long bookId, Long writerId, SentenceAppendRequest request) {
        // 1. 소설 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 2. 작성 가능 여부 검증 (도메인 로직)
        book.validateWritingPossible(writerId);

        // 3. 문장 생성
        Sentence sentence = Sentence.builder()
                .book(book)
                .writerId(writerId)
                .content(request.getContent())
                .sequenceNo(book.getCurrentSequence())
                .build();

        sentenceRepository.save(sentence);

        // 4. 소설 상태 업데이트 (순서 증가, 마지막 작성자 갱신, 완결 체크)
        book.updateStateAfterWriting(writerId);

        return sentence.getSentenceId();
    }
}
