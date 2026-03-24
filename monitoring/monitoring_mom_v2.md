# MOM — Monitoring Setup Call
**Date:** [Date]  
**Attendees:** Gaurav, Preeti (SRE), Aishwarya

---

## Context
Card domain is currently in build phase. Monitoring requirements have been identified and shared with SRE team. This is not all encompassing — additional services and log statements will be added as development progresses. Goal is for SRE to start configuring alerts incrementally with subsequent calls scheduled to review and expand coverage.

---

## Monitoring Sheet — Tab Summary

### Tab 1: ECS Availability
- Services identified for availability monitoring — note not all services are complete, more will be added
- Metrics: Task count (minimum threshold), CPU utilisation, Memory utilisation
- Thresholds: Placeholders in place — to be updated once benchmarks published

### Tab 2: Card API Endpoints
- Card API endpoints identified for latency monitoring
- Metrics: p95, p99, Average response time
- Current placeholder threshold: 500ms — to be updated once perf benchmarks published
- 4xx and 5xx error rate monitoring included
- Error rate thresholds: To be confirmed

### Tab 3: Application Logs
- Spring log.error contextual log statements identified — approximately 40-50 messages currently
- More log statements will be added as development progresses
- SRE to update configuration incrementally

### Tab 4: Infrastructure
- Kafka and SQS monitoring discussed
- To be populated — will be covered in subsequent calls

---

## Database Monitoring
- Current tooling in VC platform discussed
- Generos flagged as not target state
- To be discussed further in subsequent calls

---

## OpenSearch
- Discussed as part of broader monitoring approach
- No conclusion reached at this stage — to be revisited

---

## Hydration Monitoring
- Daily file from datalake platform expected
- Monitoring requirements to be defined — will be covered in subsequent calls

---

## Next Steps
- SRE to start configuring alerts with current placeholder thresholds
- Card domain to share updated benchmarks once perf testing complete
- Subsequent calls to be scheduled to incrementally expand monitoring coverage
