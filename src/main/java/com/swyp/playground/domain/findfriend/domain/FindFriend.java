package com.swyp.playground.domain.findfriend.domain;

import com.swyp.playground.domain.child.domain.Child;
import com.swyp.playground.domain.findfriend.dto.FindFriendRegisterRequest;
import com.swyp.playground.domain.parent.domain.Parent;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

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

    @Column(name = "playground_id")
    private String playgroundId;

    @Column(name = "playground_name")
    private String playgroundName;

    @Column(name = "title")
    private String title;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id")
    private Parent owner;

    @OneToMany(mappedBy = "findFriend")
    private List<Parent> participants = new ArrayList<>();

    public void registerParent(Parent parent) {
        participants.add(parent);
        parent.setFindFriend(this);
    }

    public void setOwner(Parent parent){
        this.owner = parent;
    }
}
