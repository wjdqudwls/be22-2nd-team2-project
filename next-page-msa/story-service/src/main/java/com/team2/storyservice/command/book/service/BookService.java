package com.team2.storyservice.command.book.service;

import com.team2.storyservice.command.book.dto.request.CreateBookRequest;
import com.team2.storyservice.command.book.dto.request.SentenceAppendRequest;
import com.team2.storyservice.command.book.entity.Book;
import com.team2.storyservice.command.book.entity.Sentence;
import com.team2.storyservice.command.book.repository.BookRepository;
import com.team2.storyservice.command.book.repository.SentenceRepository;
import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;
import com.team2.commonmodule.util.SecurityUtil;
import com.team2.storyservice.websocket.dto.BookCreatedEvent;
import com.team2.storyservice.websocket.dto.SentenceCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ?뚯꽕 Command ?쒕퉬??(?앹꽦, 臾몄옣 ?묒꽦)
 *
 * @author ?뺤쭊??
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

        private final BookRepository bookRepository;
        private final SentenceRepository sentenceRepository;
        private final SimpMessagingTemplate messagingTemplate;
        private final com.team2.storyservice.feign.MemberServiceClient memberServiceClient;
        private final com.team2.storyservice.category.repository.CategoryRepository categoryRepository;

        /**
         * ?뚯꽕 諛??앹꽦
         */
        public Long createBook(Long writerId, CreateBookRequest request) {
                // 1. Book ?앹꽦
                Book book = Book.builder()
                                .writerId(writerId)
                                .categoryId(request.getCategoryId())
                                .title(request.getTitle())
                                .maxSequence(request.getMaxSequence())
                                .status(com.team2.storyservice.command.book.entity.BookStatus.WRITING)
                                .currentSequence(1) // 1踰?臾몄옣遺???쒖옉
                                .build();

                Book savedBook = bookRepository.save(book);

                // 2. 泥?臾몄옣 ?먮룞 ?깅줉
                Sentence firstSentence = Sentence.builder()
                                .book(savedBook)
                                .writerId(writerId)
                                .content(request.getFirstSentence())
                                .sequenceNo(1)
                                .build();

                sentenceRepository.save(firstSentence);

                // 3. ?곹깭 ?낅뜲?댄듃 (1踰?臾몄옣 ?묒꽦 ?꾨즺 泥섎━ -> ?ㅼ쓬? 2踰?
                savedBook.updateStateAfterWriting(writerId);

                // 4. WebSocket 이벤트 발행 (새 챕터 생성)
                String writerNickname = memberServiceClient.getUserNickname(writerId);
                String categoryName = categoryRepository.findById(savedBook.getCategoryId())
                                .map(c -> c.getCategoryName()).orElse("카테고리");
                messagingTemplate.convertAndSend("/topic/books/new",
                                new BookCreatedEvent(
                                                savedBook.getBookId(),
                                                savedBook.getTitle(),
                                                categoryName,
                                                writerNickname));

                return savedBook.getBookId();
        }

        /**
         * 臾몄옣 ?댁뼱 ?곌린
         * 愿由ъ옄???곗냽 ?묒꽦 ?쒗븳???곸슜?섏? ?딆뒿?덈떎.
         */
        public Long appendSentence(Long bookId, Long writerId, SentenceAppendRequest request) {
                // 1. ?뚯꽕 議고쉶 (鍮꾧??????곸슜)
                Book book = bookRepository.findByIdForUpdate(bookId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                // 2. ?묒꽦 媛???щ? 寃利?(?꾨찓??濡쒖쭅) - 愿由ъ옄???곗냽 ?묒꽦 ?덉슜
                book.validateWritingPossible(writerId, SecurityUtil.isAdmin());

                // 3. 臾몄옣 ?앹꽦
                Sentence sentence = Sentence.builder()
                                .book(book)
                                .writerId(writerId)
                                .content(request.getContent())
                                .sequenceNo(book.getCurrentSequence())
                                .build();

                sentenceRepository.save(sentence);

                // 4. ?뚯꽕 ?곹깭 ?낅뜲?댄듃 (?쒖꽌 利앷?, 留덉?留??묒꽦??媛깆떊, ?꾧껐 泥댄겕)
                book.updateStateAfterWriting(writerId);

                // 5. WebSocket 이벤트 발행 (새 문장 작성)
                String writerNickname = memberServiceClient.getUserNickname(writerId);
                messagingTemplate.convertAndSend("/topic/sentences/" + bookId,
                                new SentenceCreatedEvent(
                                                bookId,
                                                sentence.getSentenceId(),
                                                sentence.getContent(),
                                                sentence.getSequenceNo(),
                                                writerNickname));

                return sentence.getSentenceId();
        }

        /**
         * ?뚯꽕 ?섎룞 ?꾧껐 (?묒꽦???꾩슜)
         */
        public void completeBook(Long bookId, Long requesterId) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                book.completeManually(requesterId);
        }

        public void updateBookTitle(Long bookId, Long requesterId, String title) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                if (!book.getWriterId().equals(requesterId) && !SecurityUtil.isAdmin()) {
                        throw new BusinessException(ErrorCode.NOT_BOOK_OWNER);
                }

                book.updateTitle(title);
        }

        public void updateSentence(Long bookId, Long sentenceId, Long requesterId, String content) {
                Sentence sentence = sentenceRepository.findById(sentenceId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                if (!sentence.getWriterId().equals(requesterId) && !SecurityUtil.isAdmin()) {
                        throw new BusinessException(ErrorCode.NOT_BOOK_OWNER);
                }

                if (!sentence.getBook().getBookId().equals(bookId)) {
                        throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
                }

                // Check if it's the last sentence
                if (sentence.getSequenceNo() != sentence.getBook().getCurrentSequence() - 1) {
                        throw new BusinessException(ErrorCode.SEQUENCE_MISMATCH); // Only last sentence can be edited
                }

                sentence.updateContent(content);
        }

        public void deleteSentence(Long bookId, Long sentenceId, Long requesterId) {
                Sentence sentence = sentenceRepository.findById(sentenceId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                if (!sentence.getWriterId().equals(requesterId) && !SecurityUtil.isAdmin()) {
                        throw new BusinessException(ErrorCode.NOT_BOOK_OWNER);
                }

                Book book = sentence.getBook();
                if (!book.getBookId().equals(bookId)) {
                        throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
                }

                // Only allow deleting the LAST sentence
                if (sentence.getSequenceNo() != book.getCurrentSequence() - 1) {
                        throw new BusinessException(ErrorCode.SEQUENCE_MISMATCH); // Only last sentence can be deleted
                }

                int deletedSequence = sentence.getSequenceNo();
                boolean isLast = (deletedSequence == book.getCurrentSequence() - 1);

                sentenceRepository.delete(sentence);

                if (!isLast) {
                        sentenceRepository.decreaseSequenceAfter(bookId, deletedSequence);
                }

                book.decrementSequence();

                // If deleted sentence was the last one, update lastWriterUserId to the previous
                // one
                if (isLast) {
                        int newLastSeq = deletedSequence - 1;
                        if (newLastSeq > 0) {
                                sentenceRepository.findByBookAndSequenceNo(book, newLastSeq)
                                                .ifPresent(s -> book.updateLastWriterUserId(s.getWriterId()));
                        } else {
                                book.updateLastWriterUserId(null);
                        }
                }
        }

        public void deleteBook(Long bookId, Long requesterId) {
                Book book = bookRepository.findById(bookId)
                                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

                if (!book.getWriterId().equals(requesterId) && !SecurityUtil.isAdmin()) {
                        throw new BusinessException(ErrorCode.NOT_BOOK_OWNER);
                }

                bookRepository.delete(book);
        }
}
