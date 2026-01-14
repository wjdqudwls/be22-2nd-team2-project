package com.team2.memberservice.query.member.mapper;

import com.team2.memberservice.query.member.dto.response.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

/**
 * 회원 Query Mapper (MyBatis)
 *
 * @author 김태형
 */
@Mapper
public interface MemberMapper {

    /**
     * 이메일로 회원 기본 정보 조회
     */
    Optional<MemberDto> findByUserEmail(@Param("userEmail") String userEmail);

    /**
     * 사용자 ID로 생성한 소설 수 조회
     */
    Integer countCreatedBooks(@Param("userId") Long userId);

    /**
     * 사용자 ID로 작성한 문장 수 조회
     */
    Integer countWrittenSentences(@Param("userId") Long userId);

    /**
     * 사용자 ID로 작성한 댓글 수 조회
     */
    Integer countWrittenComments(@Param("userId") Long userId);
}
