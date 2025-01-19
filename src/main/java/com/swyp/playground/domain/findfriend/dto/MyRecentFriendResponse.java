package com.swyp.playground.domain.findfriend.dto;

import com.swyp.playground.domain.parent.domain.ParentRoleType;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyRecentFriendResponse {

    private Long friendId;

    private String nickname;

    private ParentRoleType roleType;

    private String address;

    private String introduce;

    private String profileImg;
}
