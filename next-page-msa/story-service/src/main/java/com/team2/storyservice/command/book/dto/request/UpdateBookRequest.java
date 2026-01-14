package com.team2.storyservice.command.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBookRequest {
    @NotBlank(message = "?쒕ぉ? ?꾩닔?낅땲??")
    private String title;
}
