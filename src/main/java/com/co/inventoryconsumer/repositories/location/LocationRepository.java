package com.co.inventoryconsumer.repositories.location;

import com.co.inventoryconsumer.domain.location.LocationDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<LocationDomain, UUID> {
    List<LocationDomain> findByActiveTrue();
}
