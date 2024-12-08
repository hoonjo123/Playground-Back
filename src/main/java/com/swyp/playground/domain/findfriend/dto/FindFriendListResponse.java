package com.swyp.playground.domain.findfriend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendListResponse {

    @Builder
    public FindFriendListResponse(Long id, String playgroundName, String title, String description, String scheduleTime, String recruitmentStatus, Integer currentCount) {
        this.findFriendId = id;
        this.playgroundName = playgroundName;
        this.title = title;
        this.description = description;
        this.scheduleTime = scheduleTime;
        this.recruitmentStatus = recruitmentStatus;
        this.currentCount = currentCount;
    }
    private Long findFriendId;

    private String playgroundName;

    private String title;

    private String description;

    private String scheduleTime;

    private String recruitmentStatus;

    private Integer currentCount;

}
