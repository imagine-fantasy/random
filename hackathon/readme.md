# AutoHeal AI — Pipeline Autopilot

> Autonomous CI/CD failure diagnosis and resolution  
> JPMC SmartSDK · GPT via LLM Sandbox · firm-approved stack  
> Commercial Card Platform · Hackathon March 2026

---

## The problem

Terraform, Spinnaker and Jules pipeline failures 
are a daily reality in our development cycle. 

Our firm uses custom Terraform modules that behave 
differently from standard providers — bugs surface 
at runtime, not during code review. An approver 
signs off on the PR, everything looks correct, 
the apply fails three days later when the pipeline 
runs. By then it's an engineer's problem to diagnose 
manually — 30 to 60 minutes every single time.

---

## What we built

A CI/CD Log Triage Agent with a browser chat 
interface. Engineer asks the agent to read a 
failure log. Agent reads the file via tool calling, 
returns structured triage in under 10 seconds.

---

## Phase 1 — Built

- Browser chat UI via SmartSDK Developer UI
- Tool calling — agent reads log files directly 
  from disk, nothing hardcoded
- Structured triage output:
  - Root cause — one sentence
  - Failed stage — plan / apply / deploy
  - Plain English explanation
  - 2-3 concrete fix steps
  - Issue type: INFRA / CODE / CONFIG
- Triage report saved to file automatically
- Chat interface for engineer follow-up questions

---

## Phase 2 — Roadmap

**Orchestrator agent**  
Routes by failure type, manages agent lifecycle 
using SequentialAgent workflow.

**Code reader agent**  
Clones the repository and reads firm-specific 
custom Terraform modules. Uses semantic search 
over the codebase to understand what the module 
should do versus what the error says.

**Fix agent — agentic loop**  
Generates a candidate fix, runs local plan or 
build, validates output. If errors remain it 
iterates — fixing and validating in a loop until 
the build is clean. Only then proceeds.

**PR agent**  
Raises a pull request with the fix applied. 
Flags whether the bug was a runtime issue vs 
a review gap — visibility into where the process 
broke, not just what broke.

**Notification agent**  
Sends a Teams message or email to the on-call 
engineer with fix summary and PR link.

---

## Tech stack

| Component | Technology |
|-----------|-----------|
| Agent framework | JPMC SmartSDK |
| LLM | GPT via JPMC LLM Sandbox |
| Orchestration | SequentialAgent workflow |
| UI | SmartSDK Developer UI |
| Tools | Python function tools |
| Auth | LLM Sandbox token — no external keys |

---

## Setup
```bash
# Install SmartSDK
pip install cdaosmart-sdk

# Ensure LLM Sandbox token is active
# Start VS Code with Copilot plugin (localhost:3009)

# Run the agent
python triage.py

# Open browser
http://localhost:8000
```

Place your log file in the same folder.  
Then type in the chat:
```
Read and triage the log file: your_log.log
```

---

## Business impact

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

## Hackathon theme

Business Processes — Product Requirements accelerator

---

## What can it fix

**Terraform**
- Variable type mismatches
- Missing required variables
- Resource dependency errors
- Provider configuration issues
- Custom firm module logic gaps

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
