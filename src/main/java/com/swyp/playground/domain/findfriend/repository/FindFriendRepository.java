package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FindFriendRepository extends JpaRepository<FindFriend, Long> {
    //해당 놀이터의 친구 모집 글 모두 조회
    List<FindFriend> findAllByPlaygroundId(String playgroundId);

    //startTime이 현재 시각보다 10분 느린 친구 모집 글 모두 조회
    List<FindFriend> findByStatusAndStartTimeBefore(RecruitmentStatus status, LocalDateTime time);

    //endTime이 현재 시각과 같아진 친구 모집 글 모두 조회
    List<FindFriend> findByStatusAndEndTimeBefore(RecruitmentStatus status, LocalDateTime time);
}
