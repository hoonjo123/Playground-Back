package com.swyp.playground.domain.comment.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.comment.dto.GetCommentDto;
import com.swyp.playground.domain.comment.dto.WriteCommentDto;
import com.swyp.playground.domain.comment.repository.CommentRepository;
import com.swyp.playground.domain.note.service.NoteService;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import com.swyp.playground.common.domain.TypeChange;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private TypeChange typeChange;

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public Comment writeComment(Comment comment) {
        Long parentId = parentRepository.findByNickname(comment.getWrittenBy()).get().getParentId();
        comment.setWriterId(parentId);
        return commentRepository.save(comment);
    }

    public List<GetCommentDto> getAllComments() {
        try {
            List<Comment> comments = commentRepository.findAll();
            
            if (comments.isEmpty()) {
                return Collections.emptyList();
            }
    
            return typeChange.convertGetCommentDtoList(comments);
        } catch (Exception e) {
            throw new IllegalAccessError(e.toString());
        }
    }

    public List<GetCommentDto> getAllByMatchId(Long findFriendId) {
        List<Comment> comments = commentRepository.findAllByFindFriend_FindFriendId(findFriendId);

        return typeChange.convertGetCommentDtoList(comments);
    }
    
    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public void patchCommentById(Long id, WriteCommentDto newComment, String email) {
        try {
            Comment prevComment = commentRepository.findById(id)
                            .orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));
            
            if (prevComment.getWrittenBy() == parentRepository.findByEmail(email).get().getNickname()) {
                if (prevComment.getContent() != newComment.getContent()) {
                    prevComment.setContent(newComment.getContent());
                }
            } else {
                throw new IllegalAccessError("작성한 유저만 수정할 수 있습니다.");
            }
            
            commentRepository.save(prevComment);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void deleteComment(Long id, String user) {
        try {
            String prevUser = commentRepository.findById(id).get().getWrittenBy();
            if (prevUser.equals(user)) {
                commentRepository.deleteById(id);
            } else {
                throw new IllegalAccessError("작성한 유저만 삭제할 수 있습니다.");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
