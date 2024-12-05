package com.swyp.playground.domain.findfriend.domain;

import com.swyp.playground.domain.parent.domain.Parent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "find_friend_id")
    private FindFriend findFriend;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
}

