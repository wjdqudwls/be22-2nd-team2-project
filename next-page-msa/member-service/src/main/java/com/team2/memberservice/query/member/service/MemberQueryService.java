package com.team2.memberservice.query.member.service;

import com.team2.commonmodule.error.BusinessException;
import com.team2.commonmodule.error.ErrorCode;
import com.team2.commonmodule.feign.ReactionServiceClient;
import com.team2.commonmodule.feign.StoryServiceClient;
import com.team2.commonmodule.feign.dto.MemberReactionStatsDto;
import com.team2.commonmodule.feign.dto.MemberStoryStatsDto;
import com.team2.commonmodule.response.ApiResponse;
import com.team2.memberservice.query.member.dto.response.MemberDto;
import com.team2.memberservice.query.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 Query 서비스 (조회 전용)
 *
 * @author 김태형
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberMapper memberMapper;
    private final StoryServiceClient storyServiceClient;
    private final ReactionServiceClient reactionServiceClient;

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

        // 2. 활동 통계 조회 및 설정 (Feign Client 사용)
        Long userId = memberDto.getUserId();

        try {
            ApiResponse<MemberStoryStatsDto> storyStatsRes = storyServiceClient
                    .getMemberStoryStats(userId);
            if (storyStatsRes.isSuccess() && storyStatsRes.getData() != null) {
                memberDto.setCreatedBookCount(storyStatsRes.getData().getCreatedBookCount());
                memberDto.setWrittenSentenceCount(storyStatsRes.getData().getWrittenSentenceCount());
            }
        } catch (Exception e) {
            log.error("Story Service 통신 오류: 회원 통계 조회 실패 (userId={})", userId, e);
            // 실패 시 0으로 유지 (기본값)
        }

        try {
            ApiResponse<MemberReactionStatsDto> reactionStatsRes = reactionServiceClient
                    .getMemberReactionStats(userId);
            if (reactionStatsRes.isSuccess() && reactionStatsRes.getData() != null) {
                memberDto.setWrittenCommentCount(reactionStatsRes.getData().getWrittenCommentCount());
            }
        } catch (Exception e) {
            log.error("Reaction Service 통신 오류: 회원 통계 조회 실패 (userId={})", userId, e);
            // 실패 시 0으로 유지
        }

        return memberDto;
    }
}
