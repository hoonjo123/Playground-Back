package com.swyp.playground.common.domain;

import com.swyp.playground.domain.child.domain.Child;
import com.swyp.playground.domain.child.dto.req.ChildUpdateReqDto;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.dto.req.ParentCreateReqDto;
import com.swyp.playground.domain.parent.dto.res.ParentCreateResDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class TypeChange {

    public Parent parentCreateReqDtoToParent(ParentCreateReqDto dto, String encodedPassword) {
        return Parent.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .role(dto.getRole())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .childCount(dto.getChildCount())
                .introduce(dto.getIntroduce())
                .joinedAt(LocalDateTime.now())
                .mannerTemp(BigDecimal.valueOf(50.0))
                .build();
    }

    // Parent 엔티티를 ParentCreateResDto로 변환
    public ParentCreateResDto parentToParentCreateResDto(Parent parent) {
        return ParentCreateResDto.builder()
                .id(parent.getParentId())
                .name(parent.getName())
                .email(parent.getEmail())
                .nickname(parent.getNickname())
                .address(parent.getAddress())
                .role(parent.getRole())
                .introduce(parent.getIntroduce())
                .childCount(parent.getChildCount())
                .birthDate(parent.getBirthDate())
                .phoneNumber(parent.getPhoneNumber())
                .mannerTemp(parent.getMannerTemp())
                .children(parent.getChildren().stream()
                        .map(child -> ParentCreateResDto.ChildDto.builder()
                                .id(child.getChildId())
                                .gender(child.getGender())
                                .birthDate(child.getBirthDate())
                                .age(child.getAge())
                                .build())
                        .collect(Collectors.toList()))
                .profileImg(parent.getProfileImg())
                .build();
    }
    // ChildUpdateReqDto -> Child 엔티티 변환 (새로운 자녀 추가)
    public Child childDtoToChild(ChildUpdateReqDto dto) {
        return Child.builder()
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .age(dto.getAge())
                .build();
    }

    // 기존 Child 엔티티 업데이트
    public void updateChildFromDto(Child child, ChildUpdateReqDto dto) {
        if (dto.getGender() != null) child.setGender(dto.getGender());
        if (dto.getBirthDate() != null) child.setBirthDate(dto.getBirthDate());
        if (dto.getAge() != null) child.setAge(dto.getAge());
    }

}
