package com.swyp.playground.domain.playground.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaygroundInfoResponse {
    private List<Playground> playgrounds = new ArrayList<>();


    @Getter
    @Setter
    public static class Playground {

        @Builder
        public Playground(String id, String name, String address, String latitude, String longitude) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private String id;
        private String name;
        private String address;
        private String latitude;
        private String longitude;
    }
}
