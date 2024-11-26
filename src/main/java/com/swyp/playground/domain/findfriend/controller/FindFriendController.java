package com.swyp.playground.domain.findfriend.controller;

import com.swyp.playground.domain.findfriend.dto.FindFriendListResponse;
import com.swyp.playground.domain.findfriend.dto.FindFriendRegisterRequest;
import com.swyp.playground.domain.findfriend.service.FindFriendService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FindFriendController {

    private final FindFriendService findFriendService;


    //놀이터 별 친구 모집글 목록 조회
    @GetMapping("/find-friend/{playgroundName}")
    public ResponseEntity<Result> findFindFriendList(@PathVariable String playgroundName) {
        List<FindFriendListResponse> findFriendList = findFriendService.getFindFriendList(playgroundName);
        return ResponseEntity.ok(new Result<>(findFriendList));
    }

    //친구 모집글 등록
    @PostMapping("/find-friend/{playgroundName}")
    public Long registerFindFriend(@PathVariable String playgroundName, FindFriendRegisterRequest findFriendRegisterRequest){
        return findFriendService.registerFindFriend(playgroundName, findFriendRegisterRequest);
    }




    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
