package com.swyp.playground.domain.parent.dto.res;

import com.swyp.playground.domain.parent.domain.ParentRoleType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private ParentRoleType role;
    private Integer childCount;
    private BigDecimal mannerTemp;
}
