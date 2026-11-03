package com.multitenant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_data", indexes = {
    @Index(name = "idx_tenant", columnList = "tenant_id"),
    @Index(name = "idx_category", columnList = "category")
})
public class TenantData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "data_id")
    private String dataId;

    @Column(name = "data_name")
    private String dataName;

    @Column(name = "category")
    private String category;

    @Column(name = "data_value")
    private Double dataValue;

    @Column(name = "isolation_level")
    private String isolationLevel;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public TenantData() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getDataId() { return dataId; }
    public void setDataId(String dataId) { this.dataId = dataId; }

    public String getDataName() { return dataName; }
    public void setDataName(String dataName) { this.dataName = dataName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getDataValue() { return dataValue; }
    public void setDataValue(Double dataValue) { this.dataValue = dataValue; }

    public Double getValue() { return dataValue; }
    public void setValue(Double value) { this.dataValue = value; }

    public String getIsolationLevel() { return isolationLevel; }
    public void setIsolationLevel(String isolationLevel) { this.isolationLevel = isolationLevel; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class Builder {
        private TenantData data = new TenantData();

        public Builder tenantId(String tenantId) { data.setTenantId(tenantId); return this; }
        public Builder dataId(String dataId) { data.setDataId(dataId); return this; }
        public Builder dataName(String dataName) { data.setDataName(dataName); return this; }
        public Builder category(String category) { data.setCategory(category); return this; }
        public Builder value(Double value) { data.setDataValue(value); return this; }
        public Builder dataValue(Double dataValue) { data.setDataValue(dataValue); return this; }
        public Builder isolationLevel(String isolationLevel) { data.setIsolationLevel(isolationLevel); return this; }
        public Builder viewCount(Integer viewCount) { data.setViewCount(viewCount); return this; }
        public Builder createdAt(LocalDateTime createdAt) { data.setCreatedAt(createdAt); return this; }

        public TenantData build() {
            if (data.getCreatedAt() == null) data.setCreatedAt(LocalDateTime.now());
            if (data.getViewCount() == null) data.setViewCount(0);
            return data;
        }
    }
}