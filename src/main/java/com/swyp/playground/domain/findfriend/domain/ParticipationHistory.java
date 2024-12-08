package com.swyp.playground.domain.findfriend.domain;

import com.swyp.playground.domain.parent.domain.Parent;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ParticipationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_history_id")
    private Long id;

    @Column(name = "participation_count")
    private Integer participationCount;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "find_friend_id")
    private FindFriend findFriend;


}
