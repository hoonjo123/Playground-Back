package com.swyp.playground.domain.parent.controller;

import com.swyp.playground.common.response.CommonResponse;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.dto.req.ParentCreateReqDto;
import com.swyp.playground.domain.parent.dto.req.ParentUpdateReqDto;
import com.swyp.playground.domain.parent.dto.res.ParentCreateResDto;
import com.swyp.playground.domain.parent.service.ParentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    @PostMapping("/signup")
    public ResponseEntity<ParentCreateResDto> signUp(@Validated @RequestBody ParentCreateReqDto request) {
        ParentCreateResDto response = parentService.signUp(request);
        return ResponseEntity.ok(response);
    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/users/{id}")
    public ResponseEntity<ParentCreateResDto> getParentById(@PathVariable Long id){
        ParentCreateResDto response = parentService.getParentById(id);
        return ResponseEntity.ok(response);
    }
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/users/edit/{id}")
    public ResponseEntity<?> updateParent(@PathVariable Long id,
                                          @Validated @RequestBody ParentUpdateReqDto request,
                                          @AuthenticationPrincipal User authenticatedUser) {
        String authenticatedEmail = authenticatedUser.getUsername();
        Parent parent = parentService.getParentEntityById(id);
        if (!parent.getEmail().equals(authenticatedEmail)) {
            return ResponseEntity.status(403).body("자신의 정보만 수정할 수 있습니다.");
        }

        ParentCreateResDto response = parentService.updateParent(id, request);
        return ResponseEntity.ok(response);
    }


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/users/all")
    public ResponseEntity<List<ParentCreateResDto>> getAllParents() {
        List<ParentCreateResDto> response = parentService.getAllParents();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/users/delete/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParentById(id);
        return ResponseEntity.noContent().build();
    }
}
