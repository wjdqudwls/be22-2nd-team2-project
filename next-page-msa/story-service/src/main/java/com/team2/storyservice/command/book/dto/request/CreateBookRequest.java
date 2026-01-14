package com.team2.storyservice.command.book.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ?뚯꽕 ?앹꽦 ?붿껌 DTO
 *
 * @author ?뺤쭊??
 */
@Getter
@NoArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "?쒕ぉ? ?꾩닔 ?낅젰媛믪엯?덈떎.")
    @Size(min = 1, max = 200, message = "?쒕ぉ? 1???댁긽 200???댄븯?ъ빞 ?⑸땲??")
    private String title;

    @NotBlank(message = "移댄뀒怨좊━???꾩닔 ?낅젰媛믪엯?덈떎.")
    private String categoryId;

    @NotNull(message = "理쒕? 臾몄옣 ?섎뒗 ?꾩닔 ?낅젰媛믪엯?덈떎.")
    @Min(value = 10, message = "理쒕? 臾몄옣 ?섎뒗 10 ?댁긽?댁뼱???⑸땲??")
    @Max(value = 100, message = "理쒕? 臾몄옣 ?섎뒗 100 ?댄븯?ъ빞 ?⑸땲??")
    private Integer maxSequence;

    @NotBlank(message = "泥?臾몄옣? ?꾩닔 ?낅젰媛믪엯?덈떎.")
    @Size(min = 1, max = 200, message = "泥?臾몄옣? 1???댁긽 200???댄븯?ъ빞 ?⑸땲??")
    private String firstSentence;
}
