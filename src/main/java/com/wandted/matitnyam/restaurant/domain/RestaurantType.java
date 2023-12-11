package com.wandted.matitnyam.restaurant.domain;

import lombok.Getter;

@Getter
public enum RestaurantType {
    CAFE("/Genrestrtcate"),
    DINER("/Resrestrtcvnstr"),
    KIDS("/Kidscafe");

    final String path;

    RestaurantType(String path){
        this.path = path;
    }

    public static RestaurantType findType(String apiType) {
        return RestaurantType.valueOf(apiType);
    }
}
