package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.ParticipationHistory;
import com.swyp.playground.domain.findfriend.domain.PlayHistory;
import com.swyp.playground.domain.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationHistoryRepository extends JpaRepository<ParticipationHistory, Long> {

    Optional<ParticipationHistory> findByParentAndFindFriend(Parent parent, FindFriend findFriend);
}
