package com.swyp.playground.domain.findfriend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendOwnerInfoResponse {

    @Builder
    public FindFriendOwnerInfoResponse(Long id, String nickname, String role, String address, String profileImg) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.address = address;
        this.profileImg = profileImg;
    }

    private Long id;

    private String nickname;

    private String role;

    private String address;

    private String profileImg;
}
