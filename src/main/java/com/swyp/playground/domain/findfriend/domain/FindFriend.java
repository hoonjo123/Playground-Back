package com.swyp.playground.domain.findfriend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FIND_FRIEND_ID")
    private Long findFriendId;

    @Column(name = "PLAYGROUND_NAME")
    private String playgroundName;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "MINIMUM_COUNT")
    private Integer minimumCount;

    @Column(name = "FRIENDS_COUNT")
    private Integer friendsCount;

    @Column(name = "CURRENT_COUNT")
    private Integer currentCount;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    @Column(name = "DESCRIPTION")
    private String description;
}
