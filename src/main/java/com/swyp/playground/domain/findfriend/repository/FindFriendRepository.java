package com.swyp.playground.domain.findfriend.repository;

import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.domain.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FindFriendRepository extends JpaRepository<FindFriend, Long> {
    //해당 놀이터의 친구 모집 글 모두 조회(최신순)
    List<FindFriend> findAllByPlaygroundIdOrderByCreatedAtDesc(String playgroundId);

    //startTime이 현재 시각보다 10분 느린 친구 모집 글 모두 조회
    List<FindFriend> findByStatusAndStartTimeBefore(RecruitmentStatus status, LocalDateTime time);

    //endTime이 현재 시각과 같아진 친구 모집 글 모두 조회
    List<FindFriend> findByStatusAndEndTimeBefore(RecruitmentStatus status, LocalDateTime time);

    //특정 부모가 만든 친구 모집 글 찾기
    Optional<FindFriend> findByOwner_ParentId(Long parentId);

    //내가 모집했던 글 조회
    List<FindFriend> findAllByOwnerParentId(Long parentId);
}
