package com.swyp.playground.domain.playground.service;

import com.swyp.playground.domain.playground.dto.ApiResponse;
import com.swyp.playground.domain.playground.dto.PlaygroundInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PlaygroundService {

    //외부 API에서 필요한 정보만 response
    public PlaygroundInfoResponse externalResponseToDto(ApiResponse apiResponse) {
        PlaygroundInfoResponse playgroundInfoResponse = new PlaygroundInfoResponse();

        for (ApiResponse.Item item : apiResponse.getResponse().getBody().getItems()) {
            if(item.getLatCrtsVl() == null){
                continue;
            }
            playgroundInfoResponse.getPlaygrounds().add(PlaygroundInfoResponse.Playground.builder()
                    .name(item.getPfctNm())
                    .address(item.getRonaAddr())
                    .latitude(item.getLatCrtsVl())
                    .longitude(item.getLotCrtsVl())
                    .build()
            );
        }

        return playgroundInfoResponse;
    }
}
