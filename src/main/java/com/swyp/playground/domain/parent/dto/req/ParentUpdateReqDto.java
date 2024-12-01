package com.swyp.playground.domain.parent.dto.req;

import com.swyp.playground.domain.parent.domain.ParentRoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentUpdateReqDto {

    private String nickname;

    private String address;

    private String phoneNumber;

    private String introduce;
}