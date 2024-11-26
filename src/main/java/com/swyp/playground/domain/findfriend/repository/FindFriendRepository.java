package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.FindFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindFriendRepository extends JpaRepository<FindFriend, Long> {
    List<FindFriend> findAllByPlaygroundName(String playgroundName);
}
