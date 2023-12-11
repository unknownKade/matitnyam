package com.wandted.matitnyam.restaurant.controller;

import com.wandted.matitnyam.restaurant.domain.RestaurantType;
import com.wandted.matitnyam.restaurant.dto.Row;
import com.wandted.matitnyam.restaurant.service.RestaurantApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RestaurantApiController {

    private final RestaurantApiService restaurantApiService;

    @PostMapping("initial")
    public List<Row> getInitialData(@RequestParam("apiType") String apiType){
        return restaurantApiService.getAllAvailableData(RestaurantType.findType(apiType));
    }

}
