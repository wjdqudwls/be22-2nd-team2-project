package com.team2.commonmodule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 Entity가 공통으로 상속받는 Base Entity 클래스입니다.
 * 데이터의 생성 시간(created_at)과 수정 시간(updated_at)을 자동으로 관리합니다.
 *
 * 이 클래스를 상속받는 Entity는 별도의 시간 필드를 구현할 필요가 없습니다.
 * JPA Auditing 기능(@EntityListeners)에 의해 자동으로 값이 채워집니다.
 *
 * @author 정진호
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * 데이터 생성 일시
     * 데이터가 처음 저장될 때 자동으로 시간이 기록됩니다.
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 데이터 최종 수정 일시
     * 데이터가 변경될 때마다 자동으로 시간이 갱신됩니다.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
