package com.swyp.playground.domain.findfriend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FindFriendParticipantsListResponse {

    @Builder
    public FindFriendParticipantsListResponse(String nickname, String profileImg) {
        this.nickname = nickname;
    }

    private String nickname;

    private String profileImg;
}
