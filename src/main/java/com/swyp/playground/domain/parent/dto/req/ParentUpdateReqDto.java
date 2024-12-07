package com.swyp.playground.domain.parent.dto.req;

import com.swyp.playground.domain.child.dto.req.ChildUpdateReqDto;
import com.swyp.playground.domain.parent.domain.ParentRoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ParentUpdateReqDto {

    private String nickname;

    private String address;

    private String phoneNumber;

    private String introduce;

    private List<ChildUpdateReqDto> children;
}