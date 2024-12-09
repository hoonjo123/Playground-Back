package com.swyp.playground.domain.playground.service;

public class DistanceCalculator {
    // 두 지점의 위도와 경도를 받아 거리를 계산하여 반환
    public static String calculateDistance(double parentLatitude, double parentLongitude, String itemLat, String itemLon) {
        // 위도 및 경도 값이 null이거나 빈 값일 경우 처리
        if (itemLat == null || itemLon == null || itemLat.isEmpty() || itemLon.isEmpty()) {
            return "위치 정보 없음";
        }

        // 문자열로 된 위도와 경도를 double로 변환
        double itemLatitude = Double.parseDouble(itemLat);
        double itemLongitude = Double.parseDouble(itemLon);

        // 지구 반지름 (단위: km)
        final double EARTH_RADIUS = 6371.009; // 평균 반지름 값


        // 위도와 경도를 라디안 값으로 변환
        double latDistance = Math.toRadians(itemLatitude - parentLatitude);
        double lonDistance = Math.toRadians(itemLongitude - parentLongitude);

        // Haversine 공식을 사용하여 두 지점 간의 거리 계산
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(parentLatitude)) * Math.cos(Math.toRadians(itemLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산 (단위: km)
        double distanceInKm = EARTH_RADIUS * c;

        // m 단위로 변환
        double distanceInMeters = distanceInKm * 1000;

        // 거리에 따라 반환 형식 지정 (1km 미만은 m, 이상은 km)
        if (distanceInKm < 1) {
            return String.format("%.0f m", distanceInMeters);
        } else {
            return String.format("%.2f km", distanceInKm);
        }
    }
}
