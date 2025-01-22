package com.swyp.playground.domain.comment.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swyp.playground.domain.findfriend.domain.FindFriend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long commentId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "find_friend_id")
    private FindFriend findFriend;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writtenBy;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private Date sentAt;

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", findFriend=" + (findFriend != null ? findFriend.getFindFriendId() : "null") + // findFriend의 ID를 출력
                ", content='" + content + '\'' +
                ", writtenBy='" + writtenBy + '\'' +
                ", writerId=" + writerId +
                ", sentAt=" + sentAt +
                '}';
    }

}