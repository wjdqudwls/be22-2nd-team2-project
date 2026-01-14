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
 * 移댄뀒怨좊━ 議고쉶 而⑦듃濡ㅻ윭
 *
 * @author ?뺤쭊??
 */
@Tag(name = "Books", description = "?뚯꽕(Book) 愿??API") // ?몄쓽??Book ?쒓렇???ы븿
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Operation(summary = "移댄뀒怨좊━ 紐⑸줉 議고쉶", description = "?뚯꽕 ?앹꽦 諛?寃?됱뿉 ?ъ슜?섎뒗 移댄뀒怨좊━ ?꾩껜 紐⑸줉??議고쉶?⑸땲??")
    @GetMapping
    public ApiResponse<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ApiResponse.success(categories);
    }
}
