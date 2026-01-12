package com.team2.nextpage.command.member.repository;

import com.team2.nextpage.command.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 회원 Command Repository
 *
 * @author 김태형
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원 조회 (로그인용)
     * 
     * @param userEmail 사용자 이메일
     * @return 회원 정보 (Optional)
     */
    Optional<Member> findByUserEmail(String userEmail);

    /**
     * 이메일 중복 체크
     * 
     * @param userEmail 검사할 이메일
     * @return 존재 여부
     */
    boolean existsByUserEmail(String userEmail);

    /**
     * 닉네임 중복 체크
     * 
     * @param userNicknm 검사할 닉네임
     * @return 존재 여부
     */
    boolean existsByUserNicknm(String userNicknm);
}
