package com.swyp.playground.domain.parent.service;

import com.swyp.playground.common.S3.S3Service;
import com.swyp.playground.common.domain.TypeChange;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.dto.req.ParentCreateReqDto;
import com.swyp.playground.domain.parent.dto.req.ParentUpdateReqDto;
import com.swyp.playground.domain.parent.dto.res.ParentCreateResDto;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepository parentRepository;
    private final TypeChange typeChange;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public ParentCreateResDto signUp(ParentCreateReqDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Parent parent = typeChange.parentCreateReqDtoToParent(request, encodedPassword);
        Parent savedParent = parentRepository.save(parent);

        return typeChange.parentToParentCreateResDto(savedParent);
    }

    public ParentCreateResDto getParentById(Long id){
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + id));
        return typeChange.parentToParentCreateResDto(parent);
    }

    public List<ParentCreateResDto> getAllParents() {
        List<Parent> parents = parentRepository.findAll();
        return parents.stream()
                .map(typeChange::parentToParentCreateResDto)
                .collect(Collectors.toList());
    }
    public ParentCreateResDto updateParent(Long id, ParentUpdateReqDto request) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + id));

        if (request.getNickname() != null) {
            parent.setNickname(request.getNickname());
        }
        if (request.getAddress() != null) {
            parent.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            parent.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getIntroduce() != null) {
            parent.setIntroduce(request.getIntroduce());
        }

        Parent updatedParent = parentRepository.save(parent);
        return typeChange.parentToParentCreateResDto(updatedParent);
    }
    public void deleteParentById(Long id) {
        parentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + id));
        parentRepository.deleteById(id);
    }
    public void resetPassword(String email, String newPassword) {
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 존재하지 않습니다: " + email));

        String encodedPassword = passwordEncoder.encode(newPassword);
        parentRepository.updatePasswordByEmail(email, encodedPassword);
    }
    public String uploadProfileImage(Long parentId, MultipartFile file) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + parentId));

        try {
            // 고유한 파일 이름 생성
            String fileName = "profiles/" + parentId + "_" + file.getOriginalFilename();
            String fileUrl = s3Service.uploadFile(fileName, file.getBytes());
            parent.setProfileImg(fileUrl);

            // 저장된 Parent 업데이트
            parentRepository.save(parent);
            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }
}
