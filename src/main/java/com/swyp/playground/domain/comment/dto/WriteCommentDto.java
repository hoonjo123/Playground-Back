package com.swyp.playground.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteCommentDto {

    private Long matchId;

    private String content;

    private String writtenBy;

}
