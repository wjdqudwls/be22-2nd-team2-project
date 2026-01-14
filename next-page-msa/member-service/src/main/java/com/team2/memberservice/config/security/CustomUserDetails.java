package com.team2.memberservice.config.security;

import com.team2.memberservice.command.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails 구현체
 * Member 엔티티를 Spring Security가 이해할 수 있는 형태로 래핑
 *
 * @author 정진호
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    /**
     * 권한 목록 반환
     * ROLE_ 접두사를 자동으로 추가하여 Spring Security 표준 형식으로 변환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + member.getUserRole().name();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    /**
     * 비밀번호 반환
     */
    @Override
    public String getPassword() {
        return member.getUserPw();
    }

    /**
     * 사용자 식별자 (이메일) 반환
     */
    @Override
    public String getUsername() {
        return member.getUserEmail();
    }

    /**
     * 계정 만료 여부
     * true: 만료되지 않음
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * true: 잠기지 않음
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명(비밀번호) 만료 여부
     * true: 만료되지 않음
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * ACTIVE 상태일 때만 true
     */
    @Override
    public boolean isEnabled() {
        return member.getUserStatus().name().equals("ACTIVE");
    }

    /**
     * Member ID 반환 (편의 메서드)
     */
    public Long getUserId() {
        return member.getUserId();
    }

    /**
     * 닉네임 반환 (편의 메서드)
     */
    public String getNickname() {
        return member.getUserNicknm();
    }

    /**
     * 역할 반환 (편의 메서드)
     */
    public String getRole() {
        return member.getUserRole().name();
    }
}
