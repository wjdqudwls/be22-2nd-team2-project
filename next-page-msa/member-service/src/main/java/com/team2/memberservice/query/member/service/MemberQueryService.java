package com.team2.memberservice.query.member.service;

import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;
import com.team2.memberservice.query.member.dto.response.MemberDto;
import com.team2.memberservice.query.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 Query 서비스 (조회 전용)
 *
 * @author 김태형
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberMapper memberMapper;

    /**
     * 마이페이지 정보 조회 (활동 통계 포함)
     * 
     * @param userEmail 사용자 이메일
     * @return 회원 정보 DTO (활동 통계 포함)
     * @throws BusinessException 회원을 찾을 수 없는 경우
     */
    public MemberDto getMyPage(String userEmail) {
        // 1. 기본 회원 정보 조회
        MemberDto memberDto = memberMapper
                .findByUserEmail(userEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 활동 통계 조회 및 설정
        Long userId = memberDto.getUserId();
        memberDto.setCreatedBookCount(memberMapper.countCreatedBooks(userId));
        memberDto.setWrittenSentenceCount(memberMapper.countWrittenSentences(userId));
        memberDto.setWrittenCommentCount(memberMapper.countWrittenComments(userId));

        return memberDto;
    }
}
