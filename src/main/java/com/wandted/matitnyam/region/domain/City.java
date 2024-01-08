package com.wandted.matitnyam.region.domain;

import com.wandted.matitnyam.region.dto.CityListResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Table(name = "tb_city")
@Entity
public class City {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    public String name;

    @OneToMany(mappedBy = "city")
    public List<District> districts;

    public CityListResponse toListResponse(){
        return new CityListResponse(id, name);
    }
}
