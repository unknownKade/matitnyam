package com.wandted.matitnyam.restaurant.repository;

import com.wandted.matitnyam.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    @EntityGraph(attributePaths = {"district", "reviews"})
    Optional<Restaurant> findById(Long id);

    Restaurant findByNameAndAddress(String name, String address);

    List<Restaurant> findAll(Specification<Restaurant> specification, Sort sort);
}
