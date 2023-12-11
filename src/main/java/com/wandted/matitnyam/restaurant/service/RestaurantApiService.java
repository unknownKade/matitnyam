package com.wandted.matitnyam.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wandted.matitnyam.region.service.RegionService;
import com.wandted.matitnyam.restaurant.domain.ApiProperties;
import com.wandted.matitnyam.restaurant.domain.Restaurant;
import com.wandted.matitnyam.restaurant.domain.RestaurantType;
import com.wandted.matitnyam.restaurant.dto.ApiResponse;
import com.wandted.matitnyam.restaurant.dto.Row;
import com.wandted.matitnyam.restaurant.repository.RestaurantRepository;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Transactional
@Service
public class RestaurantApiService {

    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    private final RegionService regionService;

    private List<Row> ignoredRestaurants = new ArrayList<>();

    //API를 호출하고 데이터 저장 후 데이터 형식 무제 있는 식당 반환
    public List<Row> getAllAvailableData(RestaurantType restaurantType){
        ApiResponse response;
        int pageIndex = 1;

        do{
            response = sendApiRequest(restaurantType.getPath(), pageIndex);

            pageIndex++;

        }while(calculateMaxPageIndex(response.head.list_total_count()) > pageIndex -1);

        return ignoredRestaurants;
    }

    //해당 API 데이터를 전부 가져오기 위해 루프해야하는 페이지 수
    private int calculateMaxPageIndex(int totalCount){
        if(totalCount%ApiProperties.PAGE_SIZE == 0){
            return totalCount/ApiProperties.PAGE_SIZE;
        }

        return totalCount/ApiProperties.PAGE_SIZE + 1;
    }

    //응답 객체에서 식당 데이터를 추출하고 저장
    private List<Restaurant> saveRestaurantData(ApiResponse apiResponse){
        List<Restaurant> restaurants = new ArrayList<>();

        Restaurant restaurant = new Restaurant();
        for(Row data : apiResponse.row){
            //폐업인 경우 제외
            if (!data.BSN_STATE_NM().equals(ApiProperties.STORE_OPEN_VALUE)){
                continue;
            }

            try{
                restaurant = Restaurant.builder()
                                .name(data.BIZPLC_NM().replace("\"", ""))
                                .district(regionService.findDistrict(data.SIGUN_NM().replace("\"", ""), ApiProperties.CITY_ID))
                                .address(data.REFINE_ROADNM_ADDR().replace("\"", ""))
                                .latitude(Double.parseDouble(data.REFINE_WGS84_LAT().replace("\"", "")))
                                .longitude(Double.parseDouble(data.REFINE_WGS84_LOGT().replace("\"", "")))
                                .build();

            }catch (Exception e){
                //데이터형식에 문제 있는 식당들 데이터 반환
                ignoredRestaurants.add(data);
            }

            //데이터 저장
            try{
                restaurantRepository.save(restaurant);
            }catch (DataIntegrityViolationException e) {
                //가게 이름과 주소가 중복 된 경우 새 데이터로 덮어씌움
                boolean isDuplicate = restaurantRepository
                        .findByNameAndAddress(data.BIZPLC_NM(), data.REFINE_ROADNM_ADDR())
                        .checkDuplicate(restaurant);

                //중복된 이름과 도로명을 갖은 식당 데이터 반환
                if (isDuplicate) {
                    ignoredRestaurants.add(data);
                }
            }
        }

        return restaurants;
    }

    //API에 요청 보내기
    private ApiResponse sendApiRequest(String path, int pageIndex){
        URI uri = UriComponentsBuilder
                .fromUriString(ApiProperties.BASE_URL)
                .path(path)
                .queryParam("KEY", ApiProperties.API_KEY)
                .queryParam("pSize", ApiProperties.PAGE_SIZE)
                .queryParam("pIndex", pageIndex)
                .encode()
                .build()
                .toUri();

        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return parseXmlStringToObject(restTemplate.getForObject(uri, String.class));
    }

    //받은 요청을 객체로 변환
    private ApiResponse parseXmlStringToObject(String xml) {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        ApiResponse response;

        try{
            response = xmlMapper.readValue(xml, ApiResponse.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
