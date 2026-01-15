package com.team2.commonmodule.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * MemberStoryStatsDto
 *
 * @author Next-Page Team
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStoryStatsDto {
    private int createdBookCount;
    private int writtenSentenceCount;
}
