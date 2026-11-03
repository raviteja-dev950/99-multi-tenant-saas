# Contributing to 99-multi-tenant-saas

Thank you for your interest in contributing to Multi-Tenant SaaS Platform! This is Project 99/102 of Java 100 Projects Challenge - Tier 10 SCALE.

## Code of Conduct

By participating in this project, you agree to uphold our Code of Conduct: Be respectful, inclusive, and collaborative. We aim to create a welcoming environment for all contributors working on Multi-Tenancy.

## How Can I Contribute?

### Reporting Bugs

Use the bug_report.md template. Include:
- Steps to reproduce
- Expected vs actual behavior
- Screenshots: Home, Health, POST creates, Strategy isolated fix, JUnit Green
- Environment: OS, Java 17, Spring Boot 3.2.5, H2 tenantdb
- Tier 10 Checks: Caffeine Cache, Security Headers, Builder, Strategy, Repository, JUnit
- Bug history: value reserved keyword, \n trim fix, META-INF, Dockerfile empty

Example Bug: Isolated strategy returns [] empty because isolationLevel contains "isolated\n" from Postman Enter - Fixed with trim() in TenantDataService.createTenantData() and getTenantDataByStrategy().

### Suggesting Enhancements

Use feature_request.md template. Describe:
- Problem it solves in Multi-tenancy
- Proposed solution
- Alternatives considered
- Tier 10 Improvements
- Example: New Strategy Cross-Tenant, Factory for Tenant, Redis Cache

### Pull Requests

1. Fork the repo (https://github.com/raviteja-dev950/99-multi-tenant-saas)
2. Create your feature branch: `git checkout -b feature/AmazingFeature`
3. Commit your changes: `git commit -m 'Add AmazingFeature - Multi-tenant Isolation'`
4. Push to the branch: `git push origin feature/AmazingFeature`
5. Open a Pull Request - Include 14 screenshots, JUnit 6/6 Green, Caching proof

## Development Setup - Tier 10 SCALE Requirements

### Prerequisites

- Java 17+
- Maven 3.9+
- Eclipse IDE (Spring Tool Suite)
- Postman for API Testing
- Git
- Docker (for Render Deployment)

### Local Development Setup

1. Clone the repository:
   ```
   git clone https://github.com/raviteja-dev950/99-multi-tenant-saas.git
   cd 99-multi-tenant-saas
   ```

2. Open Eclipse:
   - File -> Import -> Existing Maven Projects -> Select folder 99-multi-tenant-saas
   - Wait for Maven dependencies download

3. Fix META-INF Bug (Common in Eclipse Spring Starter):
   - Delete folder: `src/main/resources/META-INF` if exists
   - Right Click Project -> Maven -> Update Project -> Check Force Update -> OK

4. Check application.properties (src/main/resources):
   ```
   server.port=${PORT:8101}
   spring.application.name=multi-tenant-saas
   spring.datasource.url=jdbc:h2:mem:tenantdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.jpa.hibernate.ddl-auto=create-drop
   spring.jpa.show-sql=true
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   spring.cache.type=caffeine
   spring.cache.caffeine.spec=maximumSize=1000,expireAfterAccess=600s,recordStats
   logging.level.org.springframework.cache=DEBUG
   ```

5. Run Application:
   - Right Click `MultiTenantSaasApplication.java` -> Run As -> Spring Boot App
   - Wait: Started in 10.459s / 11.034s Tomcat 8101
   - Logs: Tomcat started on port(s): 8101, Started MultiTenantSaasApplication in X seconds

6. Test Home Endpoint:
   - Browser: http://localhost:8101/
   - Expected: {"status":"UP","service":"Multi-Tenant SaaS Platform","features":["Multi-Tenant Data Isolation","Tenant-Based Data Access","Category Search","Isolation Levels: Shared, Isolated, Hybrid","Strategy Pattern","Builder Pattern","Caffeine Cache","Security Headers"],"endpoints":{...},"live_url":"https://nine9-multi-tenant-saas.onrender.com","author":" Ravi Teja"}

7. Test Health:
   - Browser: http://localhost:8101/health
   - Expected: {"status":"UP","database":"H2","databaseName":"tenantdb","caching":"Caffeine","cacheManager":"Caffeine","cacheSpec":"maximumSize=1000,expireAfterAccess=600s,recordStats","securityHeaders":"X-Content-Type-Options nosniff, X-Frame-Options DENY, X-XSS-Protection","patterns":["Builder","Strategy","Repository","Caching"],"features":["multi-tenancy","isolation-levels"]}

8. API Testing with Postman - Use Query Params Tab NOT Body:
   - POST http://localhost:8101/api/tenant-data?tenantId=tenant1&dataId=data1&dataName=Laptop%20Data&category=Electronics&dataValue=4.8&isolationLevel=isolated
   - In Postman: Select Params tab -> Enter 6 keys: tenantId, dataId, dataName, category, dataValue, isolationLevel
   - Hit Send -> 200 OK 300ms -> {"id":1,"tenantId":"tenant1","dataId":"data1","dataName":"Laptop Data","category":"Electronics","dataValue":4.8,"isolationLevel":"isolated","viewCount":0,"createdAt":"2026-11-04T00:38:22.613103","value":4.8}
   - Note: isolationLevel CLEAN without \n - trim() fix applied
   - Repeat for 3 more creates: data2 Phone Data 4.5 shared, data3 Book Data 4.9 isolated tenant2, data4 Tablet Data 4.2 hybrid
   - GET http://localhost:8101/api/tenant-data -> 4 items [id4 hybrid 4.2, id3 isolated 4.9, id2 shared 4.5, id1 isolated 4.8] - Ordered by createdAtDesc
   - GET http://localhost:8101/api/tenant-data/strategy/tenant1?type=tenant -> 3 items tenant1 data
   - GET http://localhost:8101/api/tenant-data/strategy/tenant1?type=shared -> 1 item Phone Data shared - OLD BUG WAS [] IF NO TRIM
   - GET http://localhost:8101/api/tenant-data/strategy/tenant1?type=isolated -> 1 item Laptop Data isolated - CRITICAL FIX: Was [] empty with "isolated\n" bug, now FIXED with trim() returns 1 item - PROOF
   - GET http://localhost:8101/api/tenant-data/strategy/tenant1?type=hybrid -> 3 items sorted 4.8,4.5,4.2 - Hybrid strategy DESC sorted

9. Run JUnit Tests:
   - Right Click `TenantDataServiceTest.java` -> Run As -> JUnit Test
   - Expected: Finished after 5.798 seconds, Runs: 6/6, Errors: 0, Failures: 0, Green Bar
   - Console: Builder Pattern Test Passed: Laptop Data, Format TenantData Test Passed: Tenant tenant1: Laptop Data (4.80) - isolated, Search Test Passed, Mockito Test Passed: getAllTenantData, TenantData Entity Test Passed, Mockito Test Passed: getTenantDataById
   - 6 Tests: testBuilderPattern (4.624s), testFormatTenantDataInfo (0.102s), testSearchByCategory_Mockito (0.143s), testGetAllTenantData_Mockito (0.019s), testTenantDataEntity (0.015s), testGetTenantDataById_Mockito (0.131s)

## Project Structure - Tier 10 SCALE

```
99-multi-tenant-saas/
├── src/main/java/com/multitenant
│   ├── MultiTenantSaasApplication.java (Spring Boot Main)
│   ├── config
│   │   ├── CacheConfig.java (Caffeine Cache 1000 max 600s Stats @EnableCaching)
│   │   └── SecurityHeadersConfig.java (Security Headers nosniff DENY XSS Filter)
│   ├── controller
│   │   ├── HomeController.java (GET /, /health - UP JSON, features, live URL)
│   │   └── TenantDataController.java (REST CRUD + Strategy - 12 endpoints)
│   ├── model
│   │   └── TenantData.java (Entity @Entity @Table tenant_data, Builder Pattern, data_value fix for H2 reserved keyword value, viewCount, createdAt, @Column data_value, @Transient value alias for backward compat)
│   ├── repository
│   │   └── TenantDataRepository.java (JpaRepository<TenantData,Long> - findByTenantId, findByCategory, findByCategoryContainingIgnoreCase, findByIsolationLevel, findAllByOrderByCreatedAtDesc, findByTenantIdOrderByCreatedAtDesc)
│   └── service
│       └── TenantDataService.java (Service @Service @CacheConfig, Builder, @Cacheable tenantDatas tenantData, @CacheEvict allEntries, Strategy Pattern tenant/shared/isolated/hybrid sorted, trim() fix for \n bug, formatInfo, viewCount++)
├── src/test/java
│   └── TenantDataServiceTest.java (JUnit5 + Mockito - 6/6 Green, Mockito)
├── screenshots (14 demos)
├── Dockerfile (Multi-Stage)
├── pom.xml
└── README.md
```

## Design Patterns - Tier 10 Requirements - 4 Patterns

### 1. Builder Pattern - TenantData.Builder

Builder tenantId, dataId, dataName, category, dataValue, isolationLevel, viewCount, createdAt, build().

Usage: new TenantData.Builder().tenantId("tenant1").dataId("data1").dataName("Laptop Data").category("Electronics").dataValue(4.8).isolationLevel("isolated").viewCount(0).createdAt(LocalDateTime.now()).build();

Test: testBuilderPattern - Builder Pattern Test Passed: Laptop Data - 4.624s

### 2. Strategy Pattern - 4 Strategies

Tenant: All data for tenant
Shared: Only shared isolation
Isolated: Only isolated isolation (fixed with trim())
Hybrid: Sorted by dataValue DESC 4.8->4.5->4.2

Endpoints:
- /strategy/tenant1?type=tenant -> 3 items
- /strategy/tenant1?type=shared -> 1 item Phone Data shared
- /strategy/tenant1?type=isolated -> 1 item Laptop Data isolated - CRITICAL FIX Was [] empty, now FIXED
- /strategy/tenant1?type=hybrid -> 3 items sorted 4.8,4.5,4.2 DESC

### 3. Repository Pattern

JpaRepository with custom methods: findByTenantId, findByCategory, findByCategoryContainingIgnoreCase, findByIsolationLevel, findAllByOrderByCreatedAtDesc

### 4. Caching Pattern - Caffeine

Caffeine CacheManager 1000 max 600s Stats, @Cacheable tenantDatas, tenantData, @CacheEvict allEntries

### 5. Security Pattern - SecurityHeaders

X-Content-Type-Options nosniff, X-Frame-Options DENY, X-XSS-Protection 1; mode=block

## Testing - JUnit5 + Mockito - 6/6 Green Bar 5.798s

SpringBootTest, @Mock TenantDataRepository, @InjectMocks TenantDataService, 6 tests Green.

## Bug Fixes - 9 Bugs Fixed

1. H2 Reserved Keyword value -> data_value + dataValue - Syntax error expected identifier - Fixed @Column data_value + getValue/setValue alias
2. Newline \n in isolationLevel - Isolated strategy [] empty - Fixed trim() in createTenantData and getTenantDataByStrategy - CRITICAL FIX
3. META-INF Bug - Delete folder
4. H2 Dialect Newline - Single line
5. JDBC DBC Typo - jdbc:h2:mem:tenantdb
6. Dockerfile Empty 31B - Multi-Stage fix
7. POST 400 Query Params - Params tab not Body
8. 405 Method Not Allowed - @PostMapping
9. OPENJDK NOT FOUND - eclipse-temurin:17-jdk-alpine

## Technologies - Tier 10 SCALE

Java 17, Spring Boot 3.2.5, Spring Data JPA, H2 tenantdb, Caffeine Cache 1000 max 600s Stats, JUnit5, Mockito, Maven 3.9, Docker Multi-Stage, Render Live 2m38s, Security Headers, Builder, Strategy, Repository, Caching

## Deployment - Render Live 2m38s

Dockerfile Multi-Stage, Render Settings Name nine9-multi-tenant-saas Runtime Docker Dockerfile ./Dockerfile Port 10000 ENV PORT=10000 Live URL https://nine9-multi-tenant-saas.onrender.com Build Time 2m38s

## How to Run - 6 Steps

1 Clone 2 Eclipse Import 3 Fix META-INF 4 Run Spring Boot 10.459s Tomcat 8101 5 Test Home Health POST 4 creates CLEAN without \n GET strategies isolated FIXED 1 item not [] 6 JUnit 6/6 Green 5.798s

## Flow Diagram

Client -> Controller -> Service Builder trim fix Strategy -> Repository -> H2 tenantdb -> Entity Builder data_value fix

## Author

Ravi Teja - Java Full Stack Developer - 102 Projects Challenge - Project 99/102

## License

MIT License

## Support

Give a star if helped!
\nAdditional detailed documentation for multi-tenant isolation levels, shared vs isolated vs hybrid strategies, Caffeine cache stats, security headers implementation, builder pattern usage, repository methods, JUnit Mockito tests, Docker multi-stage build, Render deployment steps, local port 8101 vs Render port 10000, H2 tenantdb configuration, trim() fix for newline bug, data_value reserved keyword fix, META-INF deletion, JDBC URL correction, POST query params usage, 405 method fix, openjdk image fix, viewCount increment logic, category search containing ignore case, count endpoint, info formatting, createdAt timestamp handling, tenantId filtering, dataId uniqueness, dataName search, dataValue sorting hybrid DESC, isolationLevel shared isolated hybrid, viewCount zero initial, createdAt now, value alias for backward compatibility, Builder pattern implementation, Strategy pattern implementation, Repository pattern implementation, Caching pattern implementation, Security pattern implementation, 14 screenshots documentation, 6 JUnit tests, 6 steps how to run, flow diagram explanation.\n\nAdditional detailed documentation for multi-tenant isolation levels, shared vs isolated vs hybrid strategies, Caffeine cache stats, security headers implementation, builder pattern usage, repository methods, JUnit Mockito tests, Docker multi-stage build, Render deployment steps, local port 8101 vs Render port 10000, H2 tenantdb configuration, trim() fix for newline bug, data_value reserved keyword fix, META-INF deletion, JDBC URL correction, POST query params usage, 405 method fix, openjdk image fix, viewCount increment logic, category search containing ignore case, count endpoint, info formatting, createdAt timestamp handling, tenantId filtering, dataId uniqueness, dataName search, dataValue sorting hybrid DESC, isolationLevel shared isolated hybrid, viewCount zero initial, createdAt now, value alias for backward compatibility, Builder pattern implementation, Strategy pattern implementation, Repository pattern implementation, Caching pattern implementation, Security pattern implementation, 14 screenshots documentation, 6 JUnit tests, 6 steps how to run, flow diagram explanation.\n