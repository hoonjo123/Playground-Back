package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {

    PlayHistory findByFindFriend_FindFriendId(Long findFriendId);

    List<PlayHistory> findByOwner_Email(String email);
}
