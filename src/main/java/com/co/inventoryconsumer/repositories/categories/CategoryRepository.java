package com.co.inventoryconsumer.repositories.categories;

import com.co.inventoryconsumer.domain.categories.CategoryDomain;
import com.co.inventoryconsumer.domain.categories.CategoryStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDomain, UUID> {

    Optional<CategoryDomain> findByName(String name);

    List<CategoryDomain> findByStatus(CategoryStatus status);

    Optional<CategoryDomain> findTopByOrderByCnsDesc();

    @Query(value = "select pg_advisory_xact_lock(8202401)", nativeQuery = true)
    void lockCategoryCnsCounter();
}
