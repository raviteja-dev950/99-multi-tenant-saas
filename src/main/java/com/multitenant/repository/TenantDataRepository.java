package com.multitenant.repository;

import com.multitenant.model.TenantData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TenantDataRepository extends JpaRepository<TenantData, Long> {

    List<TenantData> findByTenantId(String tenantId);

    List<TenantData> findByCategory(String category);

    List<TenantData> findByCategoryContainingIgnoreCase(String category);

    List<TenantData> findByIsolationLevel(String isolationLevel);

    List<TenantData> findAllByOrderByCreatedAtDesc();

    List<TenantData> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}