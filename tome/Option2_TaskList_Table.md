# Option 2: Prefetch + DB Storage Approach (Gaurav)
## Task List

---

## Phase 1: Lambda Batch Job Development

| # | Task | Estimate |
|---|------|----------|
| 1 | Design token_pool table schema (PostgreSQL/CockroachDB) | |
| 2 | Create PostgreSQL/CockroachDB instance | |
| 3 | Create token_pool table with indexes | |
| 4 | Set up database security groups | |
| 5 | Configure VPC access for Lambda | |
| 6 | Test database connectivity | |
| 7 | Create IAM role for Lambda DB access | |
| 8 | Create Lambda function project structure | |
| 9 | Set up Python environment and dependencies (boto3, psycopg2) | |
| 10 | Configure Lambda environment variables | |
| 11 | Implement TOME NS1 batch prefetch (2,500 internal tokens) | |
| 12 | Implement TOME NS2 batch prefetch (2,500 external ref IDs) | |
| 13 | Implement mapping logic (tokenize internal token on NS2) | |
| 14 | Implement bulk insert to PostgreSQL | |
| 15 | Add database connection pooling | |
| 16 | Handle database connection failures | |
| 17 | Implement CloudWatch metrics publishing | |
| 18 | Create CloudWatch dashboard | |
| 19 | Set up CloudWatch alarms (pool low, Lambda failures) | |
| 20 | Configure SNS topic for alerts | |
| 21 | Implement check_pool_health() function | |
| 22 | Write unit tests for Lambda functions | |
| 23 | Write integration tests with real TOME dev environment | |
| 24 | Create EventBridge cron rule (hourly) | |
| 25 | Deploy Lambda to Dev environment | |
| 26 | Test hourly execution | |
| 27 | Verify tokens appearing in DB | |
| 28 | Monitor for 24 hours | |
| 29 | Tune Lambda timeout and memory settings | |

---

## Phase 2: Application Integration

| # | Task | Estimate |
|---|------|----------|
| 30 | Create TokenReservationService class | |
| 31 | Implement reserveTokenPair() method with FOR UPDATE SKIP LOCKED | |
| 32 | Implement markTokenUsed() method | |
| 33 | Implement lookupInternalToken() method | |
| 34 | Handle EmptyResultDataAccessException | |
| 35 | Add fallback logic if pool empty | |
| 36 | Update CardCreationService to call reserveTokenPair() | |
| 37 | Update card creation to use reserved internal token | |
| 38 | Update card creation to store both tokens | |
| 39 | Call markTokenUsed() after card creation success | |
| 40 | Update DetokenizeService to use DB lookup first | |
| 41 | Remove NS2 lookup TOME call from detokenize | |
| 42 | Add fallback if token not in DB | |
| 43 | Update card creation API controller | |
| 44 | Update detokenize API controller | |
| 45 | Update request/response DTOs | |
| 46 | Write unit tests for token reservation | |
| 47 | Write unit tests for concurrent reservations | |
| 48 | Write unit tests for pool exhaustion scenarios | |
| 49 | Write integration tests for card creation end-to-end | |
| 50 | Write integration tests for detokenize end-to-end | |
| 51 | Set up JMeter or Gatling for load testing | |
| 52 | Run concurrency test (100 concurrent card creations) | |
| 53 | Verify no duplicate token assignments | |
| 54 | Test with 1000 concurrent requests | |
| 55 | Measure latency (p50, p95, p99) | |
| 56 | Implement cleanup job for old RESERVED tokens | |
| 57 | Add token lifecycle monitoring | |
| 58 | Implement circuit breaker for TOME calls (Resilience4j) | |
| 59 | Add retry logic with exponential backoff | |
| 60 | Implement graceful degradation (fallback to direct TOME) | |
| 61 | Add comprehensive logging with correlation IDs | |

---

## Phase 3: Testing & Deployment

| # | Task | Estimate |
|---|------|----------|
| 62 | Deploy Lambda to QA | |
| 63 | Deploy application changes to QA | |
| 64 | Run smoke tests | |
| 65 | Run regression tests | |
| 66 | Run full test suite (functional, integration, performance) | |
| 67 | Load testing (simulate 50K cards/day) | |
| 68 | Monitor pool consumption | |
| 69 | Verify no pool exhaustion | |
| 70 | Chaos testing (kill Lambda, restart ECS, simulate DB loss) | |
| 71 | Deploy Lambda to UAT | |
| 72 | Deploy application to UAT | |
| 73 | Business acceptance testing | |
| 74 | Product team sign-off | |
| 75 | Final code review | |
| 76 | Security review (PCI compliance check) | |
| 77 | Database backup verification | |
| 78 | Document rollback plan | |
| 79 | Brief on-call team | |
| 80 | Deploy Lambda to Production | |
| 81 | Verify first execution succeeds | |
| 82 | Monitor for 6 hours (6 executions) | |
| 83 | Deploy application to Production (canary - 10% traffic) | |
| 84 | Monitor for 2 hours | |
| 85 | Check error rates | |
| 86 | Verify TOME call reduction | |
| 87 | Route 50% traffic | |
| 88 | Monitor for 2 hours | |
| 89 | Route 100% traffic | |
| 90 | Monitor for 24 hours | |
| 91 | Verify pool consumption rate | |
| 92 | Verify TOME call reduction (should see 50% reduction) | |
| 93 | Check concurrency handling | |
| 94 | Validate metrics and alarms | |
| 95 | Tune Lambda refill frequency if needed | |
| 96 | Tune database indexes if needed | |
| 97 | Optimize Lambda configuration (memory, timeout) | |
| 98 | Document production runbook | |

---

## Operational Tasks

| # | Task | Estimate |
|---|------|----------|
| 99 | Monitor pool health (available tokens count) | |
| 100 | Monitor TOME call volume and success rate | |
| 101 | Monitor Lambda execution and error rate | |
| 102 | Monitor application performance (latency, errors) | |
| 103 | Tune refill frequency based on consumption | |
| 104 | Handle pool exhaustion alerts | |
| 105 | Perform database maintenance (vacuum, analyze) | |
| 106 | Update Lambda code when TOME API changes | |

---

## Summary

**Total Tasks:** 106

**Phase 1 (Lambda):** 29 tasks  
**Phase 2 (Application):** 32 tasks  
**Phase 3 (Deployment):** 37 tasks  
**Operational:** 8 tasks

**Impact at 50K cards/day:**
- TOME Calls: 160,000/day (20% reduction)
- Peak TPS: 150 TPS (75% reduction)
- ECS Compatible: Yes
- TOME Reduction: 50% for card operations
