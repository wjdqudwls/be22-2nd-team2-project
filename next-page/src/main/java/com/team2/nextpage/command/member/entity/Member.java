package com.team2.nextpage.command.member.entity;

import com.team2.nextpage.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * 회원 엔티티 (Soft Delete 적용)
 *
 * @author 김태형
 */
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET user_status = 'DELETED' WHERE user_id = ?")
@SQLRestriction("user_status = 'ACTIVE'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'USER'")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private UserStatus userStatus; // ACTIVE, DELETED

    /**
     * 회원 탈퇴 처리
     */
    public void deactivate() {
        this.userStatus = UserStatus.DELETED;
    }
}
