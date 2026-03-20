from smart_sdk.agents import LLMAgent
from smart_sdk.models import Model, ModelProvider
from smart_sdk.cli import run_adk_web

# ── Model ─────────────────────────────────────────────────
model = Model(
    name="claude-sonnet-4-5",
    provider=ModelProvider.AZURE_OPENAI,
    azure_deployment_name="claude-sonnet-4-5",
    azure_endpoint="http://localhost:3009/v1",
    azure_api_version="2024-10-21",
    auth_method=None,
    api_key="NO_KEY_REQUIRED",
)

# ── Tool ──────────────────────────────────────────────────
def read_log_file(filepath: str) -> str:
    """Reads a CI/CD failure log file and returns its contents."""
    with open(filepath, "r") as f:
        return f.read()

def save_triage_report(pipeline_name: str, report: str) -> str:
    """Saves the triage report to a file."""
    filename = f"triage_{pipeline_name}.txt"
    with open(filename, "w") as f:
        f.write(report)
    return f"Report saved to {filename}"

# ── Agent ─────────────────────────────────────────────────
triage_agent = LLMAgent(
    name="LogTriageExpert",
    description="Triages Spinnaker and Jules CI/CD failures",
    system_message="""You are a senior DevOps engineer specialising in 
Spinnaker and Jules CI/CD pipeline failures at a large bank.

When given a failure log always:
1. Read the log file using the read_log_file tool
2. Respond with:

ROOT CAUSE: one clear sentence
FAILED STAGE: which step broke
WHY: plain English explanation
FIX: 2-3 concrete steps
TYPE: INFRA / CODE / CONFIG

3. Save the report using save_triage_report tool""",
    model=model,
    tools=[read_log_file, save_triage_report],
)

# ── Developer UI ──────────────────────────────────────────
run_adk_web(
    agents_list=[triage_agent],
    port=8000,
    agent_env="dev",
)
```

Also save this as `sample_failure.log` in the same folder:
```
PIPELINE: payment-service-deploy
STAGE: Deploy to Production FAILED
TIMESTAMP: 2026-03-20 10:23:45

ERROR: Kubernetes deployment timed out after 300s
Pod payment-service-v2-7d9f8b-xkp2q failed to start
CrashLoopBackOff: container keeps restarting

Container logs:
java.lang.OutOfMemoryError: Java heap space
  at com.jpmc.payment.TokenService.processTokens(TokenService.java:142)

Previous stage: Integration Tests PASSED
Environment: PROD
Replicas requested: 3 | Replicas ready: 0
