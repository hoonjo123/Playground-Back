package com.swyp.playground.domain.comment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.comment.dto.WriteCommentDto;
import com.swyp.playground.domain.comment.repository.CommentRepository;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /* -- POST -- */
    public Comment writeComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /* -- GET -- */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    /* -- UPDATE -- */
    public void patchCommentById(Long id, WriteCommentDto newComment) {
        try {
            Comment prevComment = commentRepository.findById(id)
                            .orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
            
            if (prevComment.getContent() != newComment.getContent()) {
                prevComment.setContent(newComment.getContent());
            }
            
            commentRepository.save(prevComment);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /* -- DELETE -- */
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
