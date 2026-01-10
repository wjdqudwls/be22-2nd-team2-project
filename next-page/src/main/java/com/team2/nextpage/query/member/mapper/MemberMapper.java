package com.team2.nextpage.query.member.mapper;

import com.team2.nextpage.query.member.dto.response.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

/**
 * 회원 Query Mapper (MyBatis)
 *
 * @author 김태형
 */
@Mapper
public interface MemberMapper {

    /**
     * 내 정보 조회
     */
    Optional<MemberDto> findById(Long userId);
}
