package com.multitenant;

import com.multitenant.model.TenantData;
import com.multitenant.repository.TenantDataRepository;
import com.multitenant.service.TenantDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TenantDataServiceTest {

    @Mock private TenantDataRepository repository;
    @InjectMocks private TenantDataService service;

    @Test public void testBuilderPattern() {
        TenantData data = new TenantData.Builder().tenantId("tenant1").dataId("data1").dataName("Laptop Data").category("Electronics").dataValue(4.8).isolationLevel("isolated").createdAt(LocalDateTime.now()).build();
        assertNotNull(data); assertEquals("tenant1", data.getTenantId()); System.out.println("Builder Pattern Test Passed: " + data.getDataName());
    }
    @Test public void testTenantDataEntity() {
        TenantData data = new TenantData(); data.setTenantId("tenant1"); data.setDataId("data1"); data.setDataName("Test Data"); data.setCategory("Electronics"); data.setDataValue(4.5); data.setIsolationLevel("shared");
        assertEquals("tenant1", data.getTenantId()); System.out.println("TenantData Entity Test Passed");
    }
    @Test public void testFormatTenantDataInfo() {
        TenantData data = new TenantData.Builder().tenantId("tenant1").dataId("data1").dataName("Laptop Data").dataValue(4.8).isolationLevel("isolated").build();
        String info = service.formatTenantDataInfo(data); assertNotNull(info); assertTrue(info.contains("tenant1")); System.out.println("Format TenantData Test Passed: " + info);
    }
    @Test public void testGetTenantDataById_Mockito() {
        TenantData data = new TenantData.Builder().tenantId("tenant1").dataName("Laptop Data").dataValue(4.8).build(); data.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(data)); when(repository.save(any(TenantData.class))).thenReturn(data);
        Optional<TenantData> result = service.getTenantDataById(1L); assertTrue(result.isPresent()); verify(repository, times(1)).findById(1L); System.out.println("Mockito Test Passed: getTenantDataById");
    }
    @Test public void testSearchByCategory_Mockito() {
        TenantData data = new TenantData.Builder().tenantId("tenant1").dataName("Laptop Data").category("Electronics").dataValue(4.8).build();
        when(repository.findByCategoryContainingIgnoreCase("Electronics")).thenReturn(Arrays.asList(data));
        List<TenantData> result = service.searchByCategory("Electronics"); assertEquals(1, result.size()); System.out.println("Search Test Passed");
    }
    @Test public void testGetAllTenantData_Mockito() {
        TenantData data1 = new TenantData.Builder().tenantId("tenant1").dataName("Data1").dataValue(4.8).build(); TenantData data2 = new TenantData.Builder().tenantId("tenant2").dataName("Data2").dataValue(4.5).build();
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(data1, data2));
        List<TenantData> result = service.getAllTenantData(); assertEquals(2, result.size()); System.out.println("Mockito Test Passed: getAllTenantData");
    }
}