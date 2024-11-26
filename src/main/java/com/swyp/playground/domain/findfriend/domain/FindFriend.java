package com.swyp.playground.domain.findfriend.domain;

import com.swyp.playground.domain.child.domain.Child;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FindFriend {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "find_friend_id")
    private Long findFriendId;

    @Column(name = "playground_name")
    private String playgroundName;

    @Column(name = "title")
    private String title;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "current_count")
    private Integer currentCount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "findFriend", cascade = CascadeType.PERSIST)
    private List<Child> children = new ArrayList<>();

    public void registerChild(Child child) {
        children.add(child);
        child.setFindFriend(this);
    }
}
