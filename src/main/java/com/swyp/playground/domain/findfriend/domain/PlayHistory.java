package com.swyp.playground.domain.findfriend.domain;

import com.swyp.playground.domain.parent.domain.Parent;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "play_history_id")
    private Long playHistoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "find_friend_id")
    private FindFriend findFriend;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Parent owner;

    @ManyToMany
    @JoinTable(
            name = "play_history_participants",
            joinColumns = @JoinColumn(name = "play_history_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private List<Parent> participants;

}
