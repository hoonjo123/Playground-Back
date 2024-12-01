package com.swyp.playground.domain.findfriend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendModifyRequest {

    private String title;

    private String playgroundName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") // 날짜 포맷 추가
    private LocalDateTime startTime;

    private Integer duration;

    private String description;
}
