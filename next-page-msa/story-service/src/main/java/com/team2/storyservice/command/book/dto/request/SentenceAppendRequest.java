package com.team2.storyservice.command.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 臾몄옣 ?댁뼱?곌린 ?붿껌 DTO
 * 由대젅???뚯꽕??臾몄옣??異붽??????ъ슜?⑸땲??
 *
 * @author ?뺤쭊??
 */
@Getter
@NoArgsConstructor
public class SentenceAppendRequest {

    @NotBlank(message = "?댁슜? ?꾩닔?낅땲??")
    @Size(min = 1, max = 200, message = "臾몄옣? 1???댁긽 200???댄븯?ъ빞 ?⑸땲??")
    private String content;
}
