package com.swyp.playground.domain.comment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.comment.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment writeComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getAllByMatchId(Long matchId) {
        return commentRepository.findAllByMatchId(matchId);
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
