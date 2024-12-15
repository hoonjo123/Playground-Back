package com.swyp.playground.domain.comment.domain;

import java.util.Date;

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

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writtenBy;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private Date sentAt;
}
