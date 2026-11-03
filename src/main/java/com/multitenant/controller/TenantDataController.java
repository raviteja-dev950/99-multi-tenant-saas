package com.multitenant.controller;

import com.multitenant.model.TenantData;
import com.multitenant.service.TenantDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenant-data")
@CrossOrigin(origins = "*")
public class TenantDataController {

    @Autowired
    private TenantDataService service;

    @PostMapping
    public ResponseEntity<?> createTenantData(
            @RequestParam String tenantId,
            @RequestParam String dataId,
            @RequestParam String dataName,
            @RequestParam String category,
            @RequestParam Double dataValue,
            @RequestParam String isolationLevel) {
        try {
            TenantData created = service.createTenantData(tenantId, dataId, dataName, category, dataValue, isolationLevel);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<TenantData>> getAllTenantData() {
        List<TenantData> dataList = service.getAllTenantData();
        return ResponseEntity.ok(dataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTenantDataById(@PathVariable Long id) {
        Optional<TenantData> data = service.getTenantDataById(id);
        if (data.isPresent()) {
            return ResponseEntity.ok(data.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Tenant data not found with id: " + id);
            return ResponseEntity.status(404).body(error);
        }
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<TenantData>> getByTenantId(@PathVariable String tenantId) {
        List<TenantData> dataList = service.getTenantDataByTenantId(tenantId);
        return ResponseEntity.ok(dataList);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TenantData>> getByCategory(@PathVariable String category) {
        List<TenantData> dataList = service.getTenantDataByCategory(category);
        return ResponseEntity.ok(dataList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TenantData>> searchByCategory(@RequestParam String category) {
        List<TenantData> dataList = service.searchByCategory(category);
        return ResponseEntity.ok(dataList);
    }

    // Strategy Pattern Endpoints - Tenant vs Shared vs Isolated vs Hybrid
    @GetMapping("/strategy/{tenantId}")
    public ResponseEntity<List<TenantData>> getByStrategy(
            @PathVariable String tenantId,
            @RequestParam(defaultValue = "tenant") String type) {
        List<TenantData> dataList = service.getTenantDataByStrategy(tenantId, type);
        return ResponseEntity.ok(dataList);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", service.getTotalCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Map<String, String>> getInfo(@PathVariable Long id) {
        Optional<TenantData> dataOpt = service.getTenantDataById(id);
        if (dataOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("info", service.formatTenantDataInfo(dataOpt.get()));
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Not found");
            return ResponseEntity.status(404).body(error);
        }
    }
}