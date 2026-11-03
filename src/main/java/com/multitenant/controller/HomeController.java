package com.multitenant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("project", "99-multi-tenant-saas");
        response.put("message", "Multi-Tenant SaaS Platform - Tier 10 SCALE");
        response.put("version", "1.0.0");
        response.put("port", 8101);
        response.put("endpoints", new String[]{
            "/health",
            "/api/tenant-data",
            "/api/tenant-data/{id}",
            "/api/tenant-data/tenant/{tenantId}",
            "/api/tenant-data/category/{category}",
            "/api/tenant-data/search?category=...",
            "/api/tenant-data/strategy/{tenantId}?type=tenant|shared|isolated|hybrid",
            "/api/tenant-data/count",
            "/api/tenant-data/info/{id}"
        });
        response.put("features", new String[]{
            "Caffeine Cache 1000 max 600s Stats",
            "Security Headers nosniff DENY XSS",
            "Builder Pattern TenantData.Builder",
            "Strategy Pattern Tenant vs Shared vs Isolated vs Hybrid",
            "Repository Pattern",
            "Caching Pattern",
            "JUnit5 Mockito 6/6 Green",
            "H2 tenantdb",
            "Docker Multi-Stage"
        });
        response.put("live", "https://nine9-multi-tenant-saas.onrender.com");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("database", "H2");
        response.put("databaseName", "tenantdb");
        response.put("cache", "Caffeine");
        response.put("cacheSpec", "maximumSize=1000,expireAfterAccess=600s,recordStats");
        response.put("security", "SecurityHeaders: nosniff DENY XSS");
        response.put("patterns", new String[]{"Builder", "Strategy", "Repository", "Caching"});
        return response;
    }
}