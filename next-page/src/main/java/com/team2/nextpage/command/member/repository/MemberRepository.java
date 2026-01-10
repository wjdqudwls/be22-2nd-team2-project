package com.team2.nextpage.command.member.repository;

import com.team2.nextpage.command.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 Command Repository
 *
 * @author 김태형
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
