package com.swyp.playground.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.swyp.playground.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // matchId 대신 findFriendId로 변경
    public List<Comment> findAllByFindFriend_FindFriendId(Long findFriendId);

    @Query("SELECT c FROM Comment c WHERE c.findFriend.findFriendId = :findFriendId ORDER BY c.id DESC")
    public List<Comment> findTop3ByFindFriendId(Long findFriendId, Pageable pageable);

}
