package com.wandted.matitnyam.restaurant.domain;

import com.wandted.matitnyam.region.domain.District;
import com.wandted.matitnyam.restaurant.dto.RestaurantDetailResponse;
import com.wandted.matitnyam.restaurant.dto.RestaurantListResponse;
import com.wandted.matitnyam.review.domain.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@NoArgsConstructor
@Table(
        name = "tb_restaurant",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "address"}
                )
        }
)
@Entity
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("식당명")
    private String name;

    @Comment("시군구")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="district_id",referencedColumnName = "id")
    private District district;

    @Column(nullable = false)
    @Comment("주소")
    private String address;

    @Column(nullable = false)
    @Comment("위도")
    private Double latitude;

    @Column(nullable = false)
    @Comment("경도")
    private Double longitude;

    @OneToMany(mappedBy = "restaurant")
    List<Review> reviews;

    @Builder
    public Restaurant(String name, District district, String address, Double latitude, Double longitude){
        this.name = name;
        this.district = district;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RestaurantDetailResponse toDetailResponse(){
        return new RestaurantDetailResponse(id, name, district, address, latitude, longitude);
    }

    public RestaurantListResponse toListResponse(){
        return new RestaurantListResponse(id, name, latitude, longitude);
    }

    public boolean checkDuplicate(Restaurant restaurant) {
        boolean isSameDistrict = district == restaurant.district;
        boolean isSameLongitude = longitude.doubleValue() == restaurant.longitude;
        boolean isSameLatitude = latitude.doubleValue() == restaurant.latitude;

        return isSameDistrict && isSameLongitude && isSameLatitude;
    }
}
