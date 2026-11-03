---
name: Bug report
about: Create a report to help us improve Multi-tenant SaaS Platform
title: '[BUG] Multi-tenant SaaS - <title>'
labels: bug, Tier 10, System Design
assignees: ''
---

**Describe the bug**
A clear and concise description of what the bug is. Multi-tenant SaaS specific.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to 'GET /'
2. Click on 'GET /api/tenant-data'
3. Scroll down to 'POST /api/tenant-data?tenantId=tenant1&dataId=data1&dataName=Laptop Data&category=Electronics&dataValue=4.8&isolationLevel=isolated'
4. See error - CLEAN without \n check - isolated count [] empty bug

**Expected behavior**
A clear and concise description of what you expected to happen.
- POST should return 200 OK with CLEAN isolationLevel without \n - trim() fix
- GET /api/tenant-data should return 4 docs with viewCount
- GET /api/tenant-data/strategy/tenant1?type=tenant should return count 3
- GET /api/tenant-data/strategy/tenant1?type=shared should return count 1 shared only
- GET /api/tenant-data/strategy/tenant1?type=isolated should return count 1 isolated FIXED from [] empty - trim() fix critical
- GET /api/tenant-data/strategy/tenant1?type=hybrid should return count 3 sorted 4.8 4.5 4.2

**Logs**
If applicable, add logs to help explain your problem.
```
Tomcat started on port(s): 8101 (http) with context path ''
Started MultiTenantSaasApplication in 10.459 seconds
Builder Pattern Test Passed: Laptop Data
Isolated FIXED from [] to 1 - trim() fix verified
```

**Screenshots**
If applicable, add screenshots to help explain your problem.
- Demo1 Home Live JSON
- Demo11 Isolated FIXED from [] empty to 1 item - CLEAN proof - Most important
- Demo12 Hybrid count 3 sorted 4.8 4.5 4.2
- Demo14 JUnit Green Bar 6/6 5.798s

**Environment (please complete the following information):**
 - OS: Windows 11
 - Browser: Chrome
 - Java: 17 local, 21.0.10
 - Spring Boot: 3.2.5
 - Port: 8101 local, 10000 Render
 - Database: H2 tenantdb jdbc:h2:mem:tenantdb + data_value column fix
 - Cache: Caffeine 1000 max 600s Stats enabled
 - Security: Headers nosniff DENY XSS
 - Multi-tenant Pattern: Tenant Shared Isolated Hybrid + trim() fix
 - Deploy: Render Live 2m38s https://nine9-multi-tenant-saas.onrender.com

**Additional context**
Add any other context about the problem here.

**Critical Bug Fixes Checked:**
- [ ] H2 reserved keyword `value` -> `data_value` + dataValue with @Column(name="data_value") + getValue/setValue alias
- [ ] Isolated returns [] empty -> Fixed with trim() cleanIsolation=isolationLevel.trim() + cleanType=type.trim() + filter isolationLevel trim() - CRITICAL
- [ ] CLEAN without \n verified - Postman Enter adds \n newline bug
- [ ] META-INF folder deleted
- [ ] Docker Multi-Stage maven:3.9-eclipse-temurin-17 AS build + eclipse-temurin:17-jdk-alpine + mkdir -p data
- [ ] COPY target error fixed with Multi-Stage
- [ ] openjdk:17-jdk-slim not found -> eclipse-temurin:17-jdk-alpine
- [ ] POST 400 Query Params empty -> Set Params tab tenantId dataId dataName category dataValue isolationLevel
- [ ] JUnit 6/6 Green Bar 5.798s

**Tier 10 Compliance:**
- [ ] CaffeineCacheManager maximumSize=1000 expireAfterAccess=600s recordStats()
- [ ] SecurityHeadersConfig nosniff DENY XSS
- [ ] Builder Pattern TenantData.Builder Fluent API + trim()
- [ ] Strategy Pattern Tenant vs Shared vs Isolated vs Hybrid + trim() fix
- [ ] Repository Pattern JpaRepository + idx_tenant + idx_category + data_value fix
- [ ] CONTRIBUTING.md 12466 chars + Issues + Release + 18 Badges + Live Link + Architecture
