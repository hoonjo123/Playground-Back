package com.swyp.playground.domain.playground.service;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AddressToCoordConverter {

    public static Point getCoordinatesFromAddress(String address, String apiKey) {
        String roadAddressPattern = ".*(로|길)\\s\\d+.*"; // 도로명 주소 패턴
        String parcelAddressPattern = ".*(동|리)\\s\\d+.*"; // 지번 주소 패턴

        String searchType;
        if (address.matches(roadAddressPattern)) {
            searchType = "road"; // 도로명 주소
        } else if (address.matches(parcelAddressPattern)) {
            searchType = "parcel"; // 지번 주소
        } else {
            searchType = "unknown"; // 알 수 없는 주소 형식
        }
        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=").append(epsg);
        sb.append("&key=").append(apiKey);
        sb.append("&type=").append(searchType);
        sb.append("&address=").append(URLEncoder.encode(address, StandardCharsets.UTF_8));

        try {
            URL url = new URL(sb.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) jsonParser.parse(reader);
            JSONObject response = (JSONObject) jsonResponse.get("response");
            JSONObject result = (JSONObject) response.get("result");

            if (result != null) {
                JSONObject point = (JSONObject) result.get("point");
                double x = Double.parseDouble(point.get("x").toString());
                double y = Double.parseDouble(point.get("y").toString());
                return new Point(x, y);
            } else {
                throw new RuntimeException("주소를 변환할 수 없습니다.");
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException("API 호출 중 오류가 발생했습니다.", e);
        }
    }

    @Getter
    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
