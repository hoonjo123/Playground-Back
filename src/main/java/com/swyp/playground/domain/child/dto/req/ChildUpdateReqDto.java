package com.swyp.playground.domain.child.dto.req;


import com.swyp.playground.domain.child.domain.ChildGenderType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChildUpdateReqDto {
    private Long id; // 자녀 ID (기존 자녀 수정 시 필요)
    private ChildGenderType gender;
    private LocalDate birthDate; // "남" 또는 "여"
    private Integer age;
}
