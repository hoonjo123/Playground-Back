package com.swyp.playground.domain.findfriend.controller;

import com.swyp.playground.domain.findfriend.dto.*;
import com.swyp.playground.domain.findfriend.service.FindFriendService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class FindFriendController {

    private final FindFriendService findFriendService;


    //놀이터 별 친구 모집글 목록 조회(최신순)
    @GetMapping("/find-friend-list/{playgroundId}")
    public ResponseEntity<Result> findFindFriendList(@PathVariable String playgroundId) {
        List<FindFriendListResponse> findFriendList = findFriendService.getFindFriendList(playgroundId);
        return ResponseEntity.ok(new Result<>(findFriendList));
    }

    //친구 모집글 정보 조회
    @GetMapping("/find-friend/{findFriendId}")
    public ResponseEntity<FindFriendInfoResponse> findFindFriendInfo(@PathVariable String playgroundId, @PathVariable Long findFriendId) {
        FindFriendInfoResponse findFriendInfo = findFriendService.getFindFriendInfo(findFriendId);
        return ResponseEntity.ok(findFriendInfo);
    }

    //친구 모집글 등록
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/find-friend/{playgroundId}")
    public Long registerFindFriend(@PathVariable String playgroundId,
                                   @RequestBody @Valid FindFriendRegisterRequest findFriendRegisterRequest,
                                   @AuthenticationPrincipal UserDetails userDetails){

        String email = userDetails.getUsername();

        return findFriendService.registerFindFriend(playgroundId, email,  findFriendRegisterRequest);
    }

    //친구 모집글 수정
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/find-friend/{playgroundId}/{findFriendId}")
    public ResponseEntity<FindFriendInfoResponse> modifyFindFindFriendInfo(@PathVariable String playgroundId,
                                                                           @PathVariable Long findFriendId,
                                                                           @RequestBody @Valid FindFriendModifyRequest findFriendModifyRequest,
                                                                           @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        FindFriendInfoResponse findFriendInfo = findFriendService.modifyFindFriendInfo(playgroundId, findFriendId, email, findFriendModifyRequest);
        return ResponseEntity.ok(findFriendInfo);
    }

    //친구 모집글 삭제
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/find-friend/{playgroundId}/{findFriendId}")
    public void deleteFindFriend(@PathVariable String playgroundId,
                                 @PathVariable Long findFriendId,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        findFriendService.deleteFindFriend(findFriendId, email);
    }

    //친구 모집글 참가 및 취소
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/find-friend/{playgroundId}/{findFriendId}")
    public void participateFindFriend(@PathVariable String playgroundId,
                                      @PathVariable Long findFriendId,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam String action) {

        String email = userDetails.getUsername();
        findFriendService.actionFindFriend(playgroundId, findFriendId, email, action);
    }

    //내가 모집했던 글 조회(최신순)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/find-friend/my")
    public ResponseEntity<Result> myFindFindFriendList(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        List<FindFriendListResponse> findFriendList = findFriendService.getMyFindFriendList(email);
        return ResponseEntity.ok(new Result<>(findFriendList));
    }

    //최근 논 친구 목록
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/find-friend/recent")
    public ResponseEntity<Result> getRecentFriend(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<MyRecentFriendResponse> recentFriends = findFriendService.getRecentFriend(email);

        return ResponseEntity.ok(new Result<>(recentFriends));
    }

    //온도 남기기
    //최근 논 친구 목록
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/manner-temp")
    public void leaveMannerTemp(@AuthenticationPrincipal UserDetails userDetails, @RequestBody LeaveMannerTempRequest leaveMannerTempRequest) {

        String email = userDetails.getUsername();
        findFriendService.leaveMannerTemp(email, leaveMannerTempRequest);
    }



    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
