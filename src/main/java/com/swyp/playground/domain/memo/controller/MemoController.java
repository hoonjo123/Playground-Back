package com.swyp.playground.domain.memo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memo")
public class MemoController {

    @GetMapping
    public String testMemo() {
        return "memo";
    }
}
