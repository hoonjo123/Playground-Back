package com.swyp.playground.domain.findfriend.controller;

import com.swyp.playground.domain.findfriend.dto.FindFriendInfoResponse;
import com.swyp.playground.domain.findfriend.dto.FindFriendListResponse;
import com.swyp.playground.domain.findfriend.dto.FindFriendModifyRequest;
import com.swyp.playground.domain.findfriend.dto.FindFriendRegisterRequest;
import com.swyp.playground.domain.findfriend.service.FindFriendService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/find-friend")
public class FindFriendController {

    private final FindFriendService findFriendService;


    //놀이터 별 친구 모집글 목록 조회
    @GetMapping("/{playgroundId}")
    public ResponseEntity<Result> findFindFriendList(@PathVariable String playgroundId) {
        List<FindFriendListResponse> findFriendList = findFriendService.getFindFriendList(playgroundId);
        return ResponseEntity.ok(new Result<>(findFriendList));
    }

    //친구 모집글 정보 조회
    @GetMapping("/{playgroundId}/{findFriendId}")
    public ResponseEntity<FindFriendInfoResponse> findFindFriendInfo(@PathVariable String playgroundId, @PathVariable Long findFriendId) {
        FindFriendInfoResponse findFriendInfo = findFriendService.getFindFriendInfo(findFriendId);
        return ResponseEntity.ok(findFriendInfo);
    }

    //친구 모집글 등록(토큰)
    @PostMapping("/{playgroundId}")
    public Long registerFindFriend(@PathVariable String playgroundId,
                                   @RequestBody @Valid FindFriendRegisterRequest findFriendRegisterRequest,
                                   @AuthenticationPrincipal UserDetails userDetails){

        String email = userDetails.getUsername();

        return findFriendService.registerFindFriend(playgroundId, email,  findFriendRegisterRequest);
    }

    //친구 모집글 수정(토큰)
    @PatchMapping("/{playgroundId}/{findFriendId}")
    public ResponseEntity<FindFriendInfoResponse> modifyFindFindFriendInfo(@PathVariable String playgroundId,
                                                                           @PathVariable Long findFriendId,
                                                                           @RequestBody @Valid FindFriendModifyRequest findFriendModifyRequest,
                                                                           @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        FindFriendInfoResponse findFriendInfo = findFriendService.modifyFindFriendInfo(playgroundId, findFriendId, email, findFriendModifyRequest);
        return ResponseEntity.ok(findFriendInfo);
    }

    //친구 모집글 삭제(토큰)
    @DeleteMapping("/{playgroundId}/{findFriendId}")
    public void deleteFindFriend(@PathVariable String playgroundId,
                                 @PathVariable Long findFriendId,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        findFriendService.deleteFindFriend(findFriendId, email);
    }

    //친구 모집글 참가 및 취소(토큰)
    @PostMapping("/{playgroundId}/{findFriendId}")
    public void participateFindFriend(@PathVariable String playgroundId,
                                      @PathVariable Long findFriendId,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam String action) {

        String email = userDetails.getUsername();
        findFriendService.actionFindFriend(playgroundId, findFriendId, email, action);
    }

    //최근 논 친구 목록


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
