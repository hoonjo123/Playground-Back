package com.swyp.playground.domain.playground.controller;

import com.swyp.playground.domain.playground.dto.ApiResponse;
import com.swyp.playground.domain.playground.dto.PlaygroundInfoResponse;
import com.swyp.playground.domain.playground.exception.PlaygroundNotFoundException;
import com.swyp.playground.domain.playground.service.PlaygroundService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class playgroundController {

    @Value("${spring.api.playground-key}")
    private String playgroundApiKey; // PLAYGROUND_API_KEY

    @Value("${spring.api.address-key}")
    private String addressApiKey; // ADDRESS_API_KEY

    private final RestTemplate restTemplate;
    private final PlaygroundService playgroundService;



    //놀이터 검색
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/playgrounds")
    public ResponseEntity<PlaygroundInfoResponse> getPlaygroundInfo(@RequestParam String pfctNm, @AuthenticationPrincipal UserDetails userDetails) throws URISyntaxException {
        //페이지 번호
        int pageIndex = 1;

        //페이지 당 표시할 놀이터 수
        int recordCountPerPage = 100;

        //놀이터 장소 코드(도시공원)
        String instlPlaceCd = "A003";

        //한글 검색 인코딩
        String encodedPfctNm = URLEncoder.encode(pfctNm, StandardCharsets.UTF_8);

        String url = "http://apis.data.go.kr/1741000/pfc2/pfc/getPfctInfo2?serviceKey=" + playgroundApiKey +
                "&pageIndex=" + pageIndex +
                "&recordCountPerPage=" + recordCountPerPage +
                "&instlPlaceCd=" + instlPlaceCd +
                "&pfctNm=" + encodedPfctNm;

        URI uri = new URI(url);

        //외부 API에 요청
        ApiResponse externalResponse = restTemplate.getForObject(uri, ApiResponse.class);

        String email = userDetails.getUsername();

        PlaygroundInfoResponse playgroundInfoResponse = playgroundService.externalResponseToDto(externalResponse, email, addressApiKey);

        return ResponseEntity.ok(playgroundInfoResponse);
    }
}
