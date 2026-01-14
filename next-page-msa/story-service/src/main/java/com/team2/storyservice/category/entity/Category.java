package com.team2.storyservice.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 移댄뀒怨좊━ ?뷀떚??
 *
 * @author ?뺤쭊??
 */
@Entity
@Getter
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_id", length = 20)
    private String categoryId; // THRILLER, ROMANCE ??

    @Column(name = "category_nm", nullable = false, length = 50)
    private String categoryName; // ?ㅻ┫?? 濡쒕㎤????
}
