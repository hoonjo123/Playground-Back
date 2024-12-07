package com.swyp.playground.common.S3;


import com.swyp.playground.domain.parent.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ProfileController {
}
//    private final ParentService parentService;

//    @PostMapping("/{parentId}/upload-profile")
//    public ResponseEntity<String> uploadProfileImage(
//            @PathVariable Long parentId,
//            @RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = parentService.updateParent(parentId,);
//            return ResponseEntity.ok(fileUrl); // 업로드된 이미지 URL 반환
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("이미지 업로드 실패: " + e.getMessage());
//        }
//    }
//}