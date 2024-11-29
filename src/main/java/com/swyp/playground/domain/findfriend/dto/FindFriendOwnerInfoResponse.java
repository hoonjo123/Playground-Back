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
    public FindFriendOwnerInfoResponse(String nickname, String role, String address) {
        this.nickname = nickname;
        this.role = role;
        this.address = address;
    }

    private String nickname;

    private String role;

    private String address;
}
