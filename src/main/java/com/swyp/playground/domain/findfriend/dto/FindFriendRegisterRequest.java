package com.swyp.playground.domain.findfriend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendRegisterRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "놀이터를 등록해주세요.")
    private String playgroundName;

    @NotNull(message = "시작 시간을 입력해주세요.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") // 날짜 포맷 추가
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 선택해주세요.")
    private Integer duration;

    private String description;
}





