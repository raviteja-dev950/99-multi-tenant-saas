package com.multitenant.service;

import com.multitenant.model.TenantData;
import com.multitenant.repository.TenantDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantDataService {

    @Autowired
    private TenantDataRepository repository;

    @CacheEvict(value = {"tenantData", "tenantDatas"}, allEntries = true)
    public TenantData createTenantData(String tenantId, String dataId, String dataName, String category, Double dataValue, String isolationLevel) {
        if (isolationLevel != null) isolationLevel = isolationLevel.trim();
        if (tenantId != null) tenantId = tenantId.trim();
        if (category != null) category = category.trim();
        if (dataId != null) dataId = dataId.trim();
        
        TenantData data = new TenantData.Builder()
                .tenantId(tenantId)
                .dataId(dataId)
                .dataName(dataName)
                .category(category)
                .dataValue(dataValue)
                .isolationLevel(isolationLevel)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .build();
        return repository.save(data);
    }

    @Cacheable(value = "tenantDatas")
    public List<TenantData> getAllTenantData() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Cacheable(value = "tenantData", key = "#id")
    public Optional<TenantData> getTenantDataById(Long id) {
        Optional<TenantData> dataOpt = repository.findById(id);
        dataOpt.ifPresent(d -> {
            d.setViewCount(d.getViewCount() + 1);
            repository.save(d);
        });
        return dataOpt;
    }

    public List<TenantData> getTenantDataByTenantId(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public List<TenantData> getTenantDataByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<TenantData> searchByCategory(String category) {
        return repository.findByCategoryContainingIgnoreCase(category);
    }

    public List<TenantData> getTenantDataByStrategy(String tenantId, String strategy) {
        List<TenantData> allData = repository.findByTenantId(tenantId);
        String cleanStrategy = strategy != null ? strategy.toLowerCase().trim() : "tenant";
        switch (cleanStrategy) {
            case "tenant": return allData;
            case "shared": return allData.stream().filter(d -> d.getIsolationLevel() != null && "shared".equalsIgnoreCase(d.getIsolationLevel().trim())).collect(Collectors.toList());
            case "isolated": return allData.stream().filter(d -> d.getIsolationLevel() != null && "isolated".equalsIgnoreCase(d.getIsolationLevel().trim())).collect(Collectors.toList());
            case "hybrid": return allData.stream().sorted(Comparator.comparing(TenantData::getDataValue, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
            default: return allData;
        }
    }

    public String formatTenantDataInfo(TenantData data) {
        String cleanLevel = data.getIsolationLevel() != null ? data.getIsolationLevel().trim() : "unknown";
        return String.format("Tenant %s: %s (%.2f) - %s", data.getTenantId(), data.getDataName(), data.getDataValue(), cleanLevel);
    }

    public long getTotalCount() { return repository.count(); }
}