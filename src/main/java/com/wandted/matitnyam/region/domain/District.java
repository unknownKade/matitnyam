package com.wandted.matitnyam.region.domain;

import com.wandted.matitnyam.region.dto.DistrictListResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "tb_district")
@Entity
public class District{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    public Long latitude;

    public Long longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    public DistrictListResponse toListResponse(){
        return new DistrictListResponse(id, name);
    }
}
