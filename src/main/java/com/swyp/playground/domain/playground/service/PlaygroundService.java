package com.swyp.playground.domain.playground.service;

import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import com.swyp.playground.domain.playground.dto.ApiResponse;
import com.swyp.playground.domain.playground.dto.PlaygroundInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final ParentRepository parentRepository;

    // 외부 API에서 필요한 정보만 response
    public PlaygroundInfoResponse externalResponseToDto(ApiResponse apiResponse, String email, String addressApiKey) {
        PlaygroundInfoResponse playgroundInfoResponse = new PlaygroundInfoResponse();
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        String address = parent.getAddress();

        // Parent의 주소를 AddressToCoordConverter로 위도와 경도로 변환
        AddressToCoordConverter.Point coordinates = AddressToCoordConverter.getCoordinatesFromAddress(address, addressApiKey);
        double parentLatitude = coordinates.getX();
        double parentLongitude = coordinates.getY();

        for (ApiResponse.Item item : apiResponse.getResponse().getBody().getItems()) {
            if (item.getLatCrtsVl() == null || item.getLotCrtsVl() == null) {
                continue;
            }

            // 거리 계산
            String distance = DistanceCalculator.calculateDistance(parentLatitude, parentLongitude, item.getLotCrtsVl(), item.getLatCrtsVl());

            playgroundInfoResponse.getPlaygrounds().add(PlaygroundInfoResponse.Playground.builder()
                    .id(item.getPfctSn())
                    .name(item.getPfctNm())
                    .address(item.getRonaAddr())
                    .latitude(item.getLatCrtsVl())
                    .longitude(item.getLotCrtsVl())
                    .distance(distance) // 거리 추가
                    .build()
            );

            System.out.println("Parent Latitude: " + parentLatitude);
            System.out.println("Parent Longitude: " + parentLongitude);
            System.out.println("Item Latitude: " + item.getLatCrtsVl());
            System.out.println("Item Longitude: " + item.getLotCrtsVl());
        }

        return playgroundInfoResponse;
    }
}
