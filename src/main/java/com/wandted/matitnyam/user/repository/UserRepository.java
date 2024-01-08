package com.wandted.matitnyam.user.repository;

import com.wandted.matitnyam.user.domain.CustomUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser, String> {
    Optional<CustomUser> findByIdAndIsActiveIsTrue(String id);
}
