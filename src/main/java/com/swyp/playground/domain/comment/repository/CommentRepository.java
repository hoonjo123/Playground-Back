package com.swyp.playground.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.playground.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
