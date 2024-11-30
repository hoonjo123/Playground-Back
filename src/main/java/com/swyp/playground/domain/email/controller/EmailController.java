package com.swyp.playground.domain.email.controller;

import com.swyp.playground.common.response.CommonResponse;
import com.swyp.playground.domain.email.service.EmailService;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {

    private final EmailService emailService;
    private final ParentRepository parentRepository;

    @PostMapping("/send-email")
    public ResponseEntity<CommonResponse> emailConfirm(@RequestParam(value = "email") String email) throws Exception {
        if(parentRepository.findByEmail(email).isPresent()){
            return ResponseEntity.ok(new CommonResponse(HttpStatus.BAD_REQUEST, "중복 이메일 발생", ""));
        }
        String confirm = emailService.sendSimpleMessage(email);

        return ResponseEntity.ok(new CommonResponse(HttpStatus.OK, "메일 발송 성공", confirm));
    }
}