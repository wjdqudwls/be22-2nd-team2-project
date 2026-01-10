package com.team2.nextpage.command.book.service;

import com.team2.nextpage.command.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 소설 Command 서비스 (생성, 문장 작성)
 *
 * @author 최현지
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * 소설 방 생성
     *
     * [Hint]
     * 1. 요청 DTO(BookCreateRequest)를 인자로 받습니다.
     * 2. Book Entity를 생성합니다 (ModelMapper.map() 또는 Book.builder() 활용).
     * 3. 저장 후 생성된 Book ID를 반환합니다.
     */
    public Long createBook(/* BookCreateRequest request */) {
        // TODO: 최현지 구현 필요
        return null;
    }

    /**
     * 문장 이어 쓰기
     *
     * [Hint]
     * 1. Book을 조회합니다. (없으면 예외 발생)
     * 2. Book.validateWritingPossible(writerId)를 호출하여 도메인 규칙을 검증합니다.
     * 3. Sentence Entity를 생성합니다 (Sentence.builder()...build() 활용).
     * 4. Book.updateStateAfterWriting(writerId)를 호출하여 상태를 갱신합니다.
     */
    public Long appendSentence(Long bookId /* , SentenceAppendRequest request */) {
        // TODO: 최현지 구현 필요
        return null;
    }
}
