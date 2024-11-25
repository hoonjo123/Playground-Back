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
    @Column(name = "find_friend_id")
    private Long findFriendId;

    @Column(name = "playground_name")
    private String playgroundName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "minimum_count")
    private Integer minimumCount;

    @Column(name = "friends_count")
    private Integer friendsCount;

    @Column(name = "current_count")
    private Integer currentCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    @Column(name = "description")
    private String description;
}
