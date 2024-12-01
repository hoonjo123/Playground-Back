package com.swyp.playground.domain.comment.controller;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.comment.dto.WriteCommentDto;
import com.swyp.playground.domain.comment.service.CommentService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /* -- POST -- */
    @PostMapping
    public ResponseEntity<Comment> writeComment (@RequestBody WriteCommentDto writeCommentDto) {
        Comment targetComment = new Comment();

        targetComment.setContent(writeCommentDto.getContent());
        targetComment.setMatchId(writeCommentDto.getMatchId());
        targetComment.setWrittenBy(writeCommentDto.getWrittenBy());
        targetComment.setSentAt(getCurrentDateTime());

        targetComment = commentService.writeComment(targetComment);
        
        return new ResponseEntity<>(targetComment, HttpStatus.CREATED);
    }

    /* -- GET -- */
    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComment() {
        return new ResponseEntity<List<Comment>>(commentService.getAllComments(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Optional<Comment>> getComment(@RequestParam Long id) {
        return new ResponseEntity<Optional<Comment>>(commentService.getComment(id), HttpStatus.OK);
    }

    /* -- UPDATE -- */
    @PatchMapping
    public ResponseEntity<String> updateComment(@RequestParam Long id,
                                    @RequestBody WriteCommentDto newComment) {
            try {
                commentService.patchCommentById(id, newComment);
            } catch (Exception e) {
                System.err.println(e);
            }
        return ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.");
    }


    /* -- DELETE -- */
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestParam Long id) {
        try {
            commentService.deleteComment(id);
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }

    /* -- Other Methods -- */
    private Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return calendar.getTime();
    }
    
}
