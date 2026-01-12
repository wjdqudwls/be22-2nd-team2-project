package com.team2.nextpage.common.util;

import com.team2.nextpage.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security Context에서 현재 인증된 사용자 정보를 편리하게 가져오는 유틸리티 클래스
 *
 * @author 정진호
 */
public class SecurityUtil {

    private SecurityUtil() {
        // 유틸리티 클래스이므로 인스턴스 생성 방지
    }

    /**
     * SecurityContext에서 현재 인증된 사용자의 ID를 반환
     * 
     * @return 사용자 ID (인증되지 않은 경우 null)
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUserId();
        }

        return null;
    }

    /**
     * SecurityContext에서 현재 인증된 사용자의 이메일을 반환
     * 
     * @return 사용자 이메일 (인증되지 않은 경우 null)
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername();
        }

        return null;
    }

    /**
     * SecurityContext에서 현재 인증된 사용자의 닉네임을 반환
     * 
     * @return 사용자 닉네임 (인증되지 않은 경우 null)
     */
    public static String getCurrentUserNickname() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getNickname();
        }

        return null;
    }

    /**
     * SecurityContext에서 현재 인증된 사용자의 역할을 반환
     * 
     * @return 사용자 역할 (인증되지 않은 경우 null)
     */
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getRole();
        }

        return null;
    }

    /**
     * SecurityContext에서 현재 인증된 사용자 정보 전체를 반환
     * 
     * @return CustomUserDetails (인증되지 않은 경우 null)
     */
    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }

        return null;
    }

    /**
     * 현재 사용자가 인증되었는지 확인
     * 
     * @return 인증 여부
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
