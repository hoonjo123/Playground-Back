package com.swyp.playground.common.domain;

import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.dto.req.ParentCreateReqDto;
import com.swyp.playground.domain.parent.dto.res.ParentCreateResDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
                .mannerTemp(parent.getMannerTemp())
                .build();
    }
}
