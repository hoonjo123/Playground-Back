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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.playground.common.domain.TypeChange;
import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.comment.dto.GetCommentDto;
import com.swyp.playground.domain.comment.dto.WriteCommentDto;
import com.swyp.playground.domain.comment.service.CommentService;
import com.swyp.playground.domain.findfriend.domain.FindFriend;
import com.swyp.playground.domain.findfriend.repository.FindFriendRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private FindFriendRepository findFriendRepository;

    @PostMapping
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Comment> writeComment (@RequestBody WriteCommentDto writeCommentDto) {
        Comment targetComment = new Comment();
        FindFriend match = findFriendRepository.findById(writeCommentDto.getMatchId())
                                                .orElseThrow(()-> new IllegalArgumentException("Matching not found"));
        
        targetComment.setContent(writeCommentDto.getContent());
        targetComment.setFindFriend(match);
        targetComment.setWrittenBy(writeCommentDto.getWrittenBy());
        targetComment.setSentAt(getCurrentDateTime());

        targetComment = commentService.writeComment(targetComment);
        
        return new ResponseEntity<>(targetComment, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetCommentDto>> getAllComment() {
        return new ResponseEntity<List<GetCommentDto>>(commentService.getAllComments(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Optional<Comment>> getComment(@RequestParam Long id) {
        return new ResponseEntity<Optional<Comment>>(commentService.getComment(id), HttpStatus.OK);
    }

    @GetMapping("/match")
    public ResponseEntity<List<GetCommentDto>> getAllCommentByMatchId(@RequestParam Long id) {
        return ResponseEntity.ok(commentService.getAllByMatchId(id));
    }

    @PatchMapping
    public ResponseEntity<String> updateComment(@RequestParam Long id,
                                    @RequestBody WriteCommentDto newComment,
                                    @AuthenticationPrincipal User user) {
        String email = user.getUsername();
            try {
                commentService.patchCommentById(id, newComment, email);
            } catch (Exception e) {
                System.err.println(e);
            }
        return ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteComment(@RequestParam Long id,
                                    @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        try {
            commentService.deleteComment(id, email);
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
