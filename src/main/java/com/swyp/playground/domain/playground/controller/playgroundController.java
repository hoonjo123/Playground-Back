package com.swyp.playground.domain.playground.controller;

import com.swyp.playground.domain.playground.dto.ApiResponse;
import com.swyp.playground.domain.playground.dto.PlaygroundInfoResponse;
import com.swyp.playground.domain.playground.exception.PlaygroundNotFoundException;
import com.swyp.playground.domain.playground.service.PlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
public class playgroundController {

    @Value("${api.key}")
    private String serviceKey;  // API 인증키
    private final RestTemplate restTemplate;
    private final PlaygroundService playgroundService;


    //놀이터 검색
    @GetMapping("/playgrounds")
    public ResponseEntity<PlaygroundInfoResponse> getPlaygroundInfo(@RequestParam String pfctNm) throws URISyntaxException {

        //페이지 번호
        int pageIndex = 1;

        //페이지 당 표시할 놀이터 수
        int recordCountPerPage = 100;

        //놀이터 장소 코드(도시공원)
        String instlPlaceCd = "A003";

        //한글 검색 인코딩
        String encodedPfctNm = URLEncoder.encode(pfctNm, StandardCharsets.UTF_8);

        String url = "http://apis.data.go.kr/1741000/pfc2/pfc/getPfctInfo2?serviceKey=" + serviceKey +
                "&pageIndex=" + pageIndex +
                "&recordCountPerPage=" + recordCountPerPage +
                "&instlPlaceCd=" + instlPlaceCd +
                "&pfctNm=" + encodedPfctNm;

        URI uri = new URI(url);

        //외부 API에 요청
        ApiResponse externalResponse = restTemplate.getForObject(uri, ApiResponse.class);

        if (externalResponse.getResponse().getBody().getTotalCnt() == 0) {
            System.out.println("hi");
            throw new PlaygroundNotFoundException("검색하신 놀이터가 존재하지 않습니다.");
        }

        PlaygroundInfoResponse playgroundInfoResponse = playgroundService.externalResponseToDto(externalResponse);

        return ResponseEntity.ok(playgroundInfoResponse);
    }


}
