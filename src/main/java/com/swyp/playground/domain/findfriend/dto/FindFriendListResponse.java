package com.swyp.playground.domain.findfriend.dto;


import com.swyp.playground.domain.findfriend.domain.RecruitmentStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendListResponse {

    @Builder
    public FindFriendListResponse(String title, String description, String scheduleTime, String recruitmentStatus, Integer currentCount) {
        this.title = title;
        this.description = description;
        this.scheduleTime = scheduleTime;
        this.recruitmentStatus = recruitmentStatus;
        this.currentCount = currentCount;
    }

    private String title;

    private String description;

    private String scheduleTime;

    private String recruitmentStatus;

    private Integer currentCount;

}
