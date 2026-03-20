# AutoHeal AI — Pipeline Autopilot

> Autonomous CI/CD failure diagnosis and resolution  
> JPMC SmartSDK · GPT via LLM Sandbox · firm-approved stack  
> Commercial Card Platform · Hackathon March 2026

---

## Team Name and Members

AutoHeal AI — [Your SID]

---

## Problem Statement

Terraform, Spinnaker and Jules pipeline failures are a daily reality in our development cycle. Our firm uses custom Terraform modules that behave differently from standard providers — failures surface at runtime not during code review. The module might have an edge case, it might not be configured correctly for a specific use case, or there might be a mismatch that only appears when the pipeline actually runs. Engineers spend 30 to 60 minutes per failure switching between dashboards, logs, IDEs and module documentation to diagnose the issue. Nobody currently flags whether a failure was a runtime edge case or a usage gap — recurring failures compound hidden costs and delay delivery.

---

## Solution Summary

AutoHeal AI is a CI/CD Log Triage Agent built on JPMC SmartSDK with GPT via our LLM Sandbox.

**Phase 1 — built and running today:**

Engineer asks the agent to read a failure log. SmartSDK tool calling reads the file directly off disk. Agent returns structured triage in under 10 seconds — root cause, failed stage, plain English explanation, concrete fix steps, issue type. Triage report saved automatically. Browser chat interface available for follow-up questions.

**Phase 2 — roadmap:**

Full autonomous multi-agent pipeline. Orchestrator routes failures via SequentialAgent workflow. Code reader agent clones repo and reads firm custom modules using semantic search — understanding what the module is supposed to do, how it is being called, and whether there is an edge case or misconfiguration. Fix agent runs an agentic loop — generates fix, validates locally, iterates until build is clean. PR agent raises pull request. Notification agent sends Teams or email alert.

---

## Architecture Diagram and Stack

Multi-agent system built on JPMC SmartSDK:

```
Pipeline failure trigger (Spinnaker / Jules / Terraform)
        ↓
Orchestrator Agent
Routes by failure type · manages agent lifecycle
SequentialAgent workflow · state across agents
        ↓
Log Triage Agent ✓ BUILT
SmartSDK LLMAgent · tool calling
Reads log file off disk · structured output
        ↓
Code Reader Agent — ROADMAP
Clones repo · reads firm custom Terraform modules
Semantic search · checks module usage and edge cases
        ↓
Fix Agent — ROADMAP
Agentic loop · generates fix · runs local plan or build
Validates output · iterates until build is clean
        ↓
PR Agent — ROADMAP
Raises PR · flags runtime edge case vs usage gap
        ↓
Notification Agent — ROADMAP
Teams message or email · fix summary · PR link
```

**Tech Stack:**

| Component | Technology |
|-----------|-----------|
| Agent framework | JPMC SmartSDK |
| LLM | GPT via JPMC LLM Sandbox |
| Orchestration | SequentialAgent workflow |
| UI | SmartSDK Developer UI |
| Tools | Python function tools |
| Auth | LLM Sandbox token — no external keys |

---

## Setup / Run Instructions

```bash
# Step 1 — Install SmartSDK
pip install cdaosmart-sdk

# Step 2 — Ensure JPMC LLM Sandbox token is active

# Step 3 — Start VS Code with Copilot plugin
# This starts the bridge on localhost:3009

# Step 4 — Run the agent
python triage.py

# Step 5 — Open browser
http://localhost:8000

# Step 6 — Place your failure log file in the same folder

# Step 7 — Type in chat
Read and triage the log file: your_log_file.log
```

---

## Demo Link

[Paste your Zoom recording link here]

---

## What Can It Fix

**Terraform**
- Variable type mismatches
- Missing required variables
- Resource dependency errors
- Provider configuration issues
- Custom firm module edge cases and usage gaps

**Spinnaker**
- Bake stage failures
- Deploy stage Terraform errors
- Expression evaluation failures
- Webhook misconfigurations

**Jules**
- Build and test stage failures
- Deployment manifest errors
- Environment variable mismatches
- Secret reference errors

**General CI/CD**
- Dockerfile syntax errors
- Malformed YAML or JSON
- IAM and policy errors
- Network and security group conflicts

---

## Measurable Impact

| Metric | Before | After |
|--------|--------|-------|
| Identify failed module | 10-15 min | < 5 sec |
| Diagnose root cause | 15-20 min | < 10 sec |
| Craft a fix | 10-20 min | < 15 sec |
| Total MTTR per failure | 45-60 min | < 2 min |
| Tools required | 4-6 | 1 chat interface |
| Knowledge needed | Senior engineer | Any team member |

**MTTR cut 95% — from 60 minutes to under 2 minutes.**

---

## Limitations and Next Steps

**Current limitations:**
- Phase 1 reads and triages logs only — does not yet apply fixes automatically
- Tool calling requires localhost:3009 bridge via VS Code Copilot plugin
- Single log file per session currently

**Next steps:**
- Phase 2 code reader agent with RAG over firm codebase and custom Terraform modules
- Fix agent agentic loop with local validation
- PR automation via GitHub integration
- Teams and email notification agent
- Webhook trigger from Spinnaker and Jules for fully autonomous operation
- Multi-repo support and compliance audit trails

---

## Hackathon Theme

Business Processes — Product Requirements accelerator
