package com.team2.nextpage.command.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SentenceAppendRequest {

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 10, max = 200, message = "문장은 10자 이상 200자 이하여야 합니다.")
    private String content;

    @NotNull(message = "작성자 ID는 필수입니다.")
    private Long writerId;
}
