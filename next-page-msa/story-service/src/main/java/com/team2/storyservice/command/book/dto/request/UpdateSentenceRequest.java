package com.team2.storyservice.command.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateSentenceRequest {
    @NotBlank(message = "?댁슜? ?꾩닔?낅땲??")
    private String content;
}
