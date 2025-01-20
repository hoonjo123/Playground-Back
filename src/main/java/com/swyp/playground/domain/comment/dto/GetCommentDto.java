package com.swyp.playground.domain.comment.dto;

import java.util.Date;

import com.swyp.playground.domain.comment.domain.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCommentDto {
    private Long commentId;

    private Long matchId;

    private String content;

    private String writtenBy;

    private Long writerId;

    private Date sentAt;

    @Builder
    public GetCommentDto(Long commentId, Long matchId, String content, String writtenBy, Long writerId, Date sentAt) {
        this.commentId = commentId;
        this.matchId = matchId;
        this.content = content;
        this.writtenBy = writtenBy;
        this.writerId = writerId;
        this.sentAt = sentAt;
    }
}
