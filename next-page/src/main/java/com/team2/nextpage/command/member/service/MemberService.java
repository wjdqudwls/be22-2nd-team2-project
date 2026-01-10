package com.team2.nextpage.command.member.service;

import com.team2.nextpage.command.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 Command 서비스 (회원가입, 탈퇴 등)
 *
 * @author 김태형
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 신규 회원 가입
     * 
     * [Hint]
     * 1. 요청 DTO(SignUpRequest)를 인자로 받습니다.
     * 2. 이메일 중복 체크를 수행합니다 (Repository).
     * 3. 비밀번호는 반드시 BCrypt 등으로 암호화해야 합니다.
     * 4. Member Entity를 생성하여 저장합니다 (ModelMapper.map() 또는 Member.builder() 활용).
     * 5. 가입된 회원의 ID(PK)를 반환합니다.
     */
    public Long signUp(/* SignUpRequest request */) {
        // TODO: 김태형 구현 필요
        return null;
    }

    /**
     * 회원 탈퇴 (Soft Delete)
     *
     * [Hint]
     * 1. SecurityContext에서 현재 로그인한 사용자 ID를 가져오거나 인자로 받습니다.
     * 2. repository.deleteById(userId)를 호출합니다.
     * 3. Entity에 정의된 @SQLDelete에 의해 자동으로 'DELETED' 상태로 업데이트됩니다.
     */
    public void withdraw(Long userId) {
        // TODO: 김태형 구현 필요
    }
}
