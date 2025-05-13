package com.voyagerss.persist.dto;

public record KakaoAddressDocument(
    String address_name,
    String address_type,
    String y,
    String x,
    RoadAddress road_address,
    Address address
) {
    public record RoadAddress(
        String address_name,
        String building_name,
        String main_building_no,
        String sub_building_no,
        String region_1depth_name,
        String region_2depth_name,
        String region_3depth_name,
        String road_name,
        String underground_yn,
        String x,
        String y,
        String zone_no
    ) {}

    public record Address(
        String address_name,
        String b_code,
        String h_code,
        String main_address_no,
        String sub_address_no,
        String mountain_yn,
        String region_1depth_name,
        String region_2depth_name,
        String region_3depth_name,
        String region_3depth_h_name,
        String x,
        String y
    ) {}
} 
