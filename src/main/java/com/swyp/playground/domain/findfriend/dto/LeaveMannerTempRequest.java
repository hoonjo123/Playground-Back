package com.swyp.playground.domain.findfriend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class LeaveMannerTempRequest {

    private String nickname;

    private BigDecimal mannerTemp;

}
