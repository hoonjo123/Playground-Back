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

    //친구 모집글 등록 ++추후 토큰 필요
    @PostMapping("/{playgroundId}")
    public Long registerFindFriend(@PathVariable String playgroundId,
                                   @RequestBody @Valid FindFriendRegisterRequest findFriendRegisterRequest){
        return findFriendService.registerFindFriend(playgroundId, findFriendRegisterRequest);
    }

    //친구 모집글 수정 ++추후 토큰 필요
    @PatchMapping("/{playgroundId}/{findFriendId}")
    public ResponseEntity<FindFriendInfoResponse> modifyFindFindFriendInfo(@PathVariable String playgroundId,
                                                                           @PathVariable Long findFriendId,
                                                                           @RequestBody @Valid FindFriendModifyRequest findFriendModifyRequest) {
        FindFriendInfoResponse findFriendInfo = findFriendService.modifyFindFriendInfo(playgroundId, findFriendId, findFriendModifyRequest);
        return ResponseEntity.ok(findFriendInfo);
    }

    //친구 모집글 삭제 ++추후 토큰 필요
    @DeleteMapping("/{playgroundId}/{findFriendId}")
    public void deleteFindFriend(@PathVariable String playgroundId,
                                 @PathVariable Long findFriendId) {
        findFriendService.deleteFindFriend(findFriendId);
    }



    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
