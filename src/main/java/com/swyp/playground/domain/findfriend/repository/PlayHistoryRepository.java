package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.PlayHistory;
import com.swyp.playground.domain.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    List<PlayHistory> findByFindFriend(FindFriend findFriend);

    List<PlayHistory> findByParent(Parent parent);

}
