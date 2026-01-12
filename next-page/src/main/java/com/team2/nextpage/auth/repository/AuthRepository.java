package com.team2.nextpage.auth.repository;

import com.team2.nextpage.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Refresh Token Command Repository
 *
 * @author 김태형
 */
public interface AuthRepository extends JpaRepository<RefreshToken, String> {

    /**
     * 사용자 이메일로 Refresh Token 조회
     *
     * @param userEmail 사용자 이메일
     * @return RefreshToken (Optional)
     */
    Optional<RefreshToken> findByUserEmail(String userEmail);

    /**
     * 사용자 이메일로 Refresh Token 삭제
     *
     * @param userEmail 사용자 이메일
     */
    void deleteByUserEmail(String userEmail);
}
