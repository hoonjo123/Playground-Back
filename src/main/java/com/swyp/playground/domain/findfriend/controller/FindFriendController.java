package com.swyp.playground.domain.findfriend.controller;

import com.swyp.playground.domain.findfriend.dto.FindFriendListResponse;
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
public class FindFriendController {

    private final FindFriendService findFriendService;


    //놀이터 별 친구 모집글 목록 조회
    @GetMapping("/find-friend/{playgroundId}")
    public ResponseEntity<Result> findFindFriendList(@PathVariable String playgroundId) {
        List<FindFriendListResponse> findFriendList = findFriendService.getFindFriendList(playgroundId);
        return ResponseEntity.ok(new Result<>(findFriendList));
    }

    //친구 모집글 등록
    @PostMapping("/find-friend/{playgroundId}")
    public Long registerFindFriend(@PathVariable String playgroundId,
                                   @RequestBody @Valid FindFriendRegisterRequest findFriendRegisterRequest){
        return findFriendService.registerFindFriend(playgroundId, findFriendRegisterRequest);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
