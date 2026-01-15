package com.team2.storyservice.category.controller;

import com.team2.storyservice.category.entity.Category;
import com.team2.storyservice.category.repository.CategoryRepository;
import com.team2.commonmodule.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 카테고리 조회 컨트롤러
 *
 * @author 정진호
 */
@Tag(name = "Books", description = "소설(Book) 관련 API") // 의미상 Book 태그에 포함
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Operation(summary = "카테고리 목록 조회", description = "소설 생성 및 검색에 사용되는 카테고리 전체 목록을 조회합니다")
    @GetMapping
    public ApiResponse<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ApiResponse.success(categories);
    }

}
