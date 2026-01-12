package com.team2.nextpage.auth.service;

import com.team2.nextpage.command.member.entity.Member;
import com.team2.nextpage.command.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Spring Security UserDetailsService 구현체
 * 사용자 이메일로 회원 정보를 조회하여 인증에 사용합니다.
 *
 * @author 김태형
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  /**
   * 사용자 이메일로 UserDetails 조회
   * Spring Security 인증 과정에서 호출됩니다.
   *
   * @param userEmail 사용자 이메일 (username으로 사용)
   * @return UserDetails 객체
   * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
   */
  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    log.debug("사용자 조회 시도: {}", userEmail);

    Member member = memberRepository.findByUserEmail(userEmail)
        .orElseThrow(() -> {
          log.warn("사용자를 찾을 수 없습니다: {}", userEmail);
          return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail);
        });

    log.debug("사용자 조회 성공: {}, Role: {}", member.getUserEmail(), member.getUserRole());

    return new User(
        member.getUserEmail(),
        member.getUserPw(),
        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getUserRole().name())));
  }
}
