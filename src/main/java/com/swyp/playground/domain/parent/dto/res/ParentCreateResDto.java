package com.swyp.playground.domain.parent.dto.res;

import com.swyp.playground.domain.child.domain.ChildGenderType;
import com.swyp.playground.domain.parent.domain.ParentRoleType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ParentCreateResDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String address;
    private String introduce;
    private LocalDate birthDate;
    private String phoneNumber;
    private ParentRoleType role;
    private Integer childCount;
    private BigDecimal mannerTemp;
    private List<ChildDto> children; // 자녀 정보 추가

    @Getter
    @Builder
    public static class ChildDto {
        private Long id;
        private ChildGenderType gender;
        private LocalDate birthDate;
        private Integer age;
    }
}
