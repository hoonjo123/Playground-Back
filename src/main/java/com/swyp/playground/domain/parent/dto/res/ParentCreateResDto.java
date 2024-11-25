package com.swyp.playground.domain.parent.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParentCreateResDto {
    private Long id;
    private String name;
    private String email;
    private String nickname;
}
