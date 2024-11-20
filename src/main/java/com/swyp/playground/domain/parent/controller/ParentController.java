package com.swyp.playground.domain.parent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class ParentController {
    @GetMapping("/test")
    public String test(){
        return "스위프";
    }
}
