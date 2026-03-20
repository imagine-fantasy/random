from smart_sdk.agents import LLMAgent
from smart_sdk.models import Model, ModelProvider
from smart_sdk.tools import Tool
from smart_sdk.cli import run_adk_web

model = Model(
    name="claude-sonnet-4-5",
    provider=ModelProvider.AZURE_OPENAI,
    azure_deployment_name="claude-sonnet-4-5",
    azure_endpoint="http://localhost:3009/v1",
    azure_api_version="2024-10-21",
    auth_method=None,
    api_key="NO_KEY_REQUIRED",
)

# ── Raw functions ─────────────────────────────────────────
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

# ── Wrap in Tool class ────────────────────────────────────
read_tool = Tool(
    tool_name="read_log_file",
    func=read_log_file,
)

save_tool = Tool(
    tool_name="save_triage_report",
    func=save_triage_report,
)

# ── Agent ─────────────────────────────────────────────────
triage_agent = LLMAgent(
    name="LogTriageExpert",
    description="Triages Spinnaker and Jules CI/CD failures",
    system_message="""You are a senior DevOps engineer specialising in 
Spinnaker and Jules CI/CD pipeline failures at a large bank.

You have two tools:
- read_log_file: reads a log file from disk
- save_triage_report: saves your analysis to a file

When asked to triage a log:
1. Use read_log_file to read it
2. Respond with:

ROOT CAUSE: one clear sentence
FAILED STAGE: which step broke
WHY: plain English
FIX: 2-3 concrete steps
TYPE: INFRA / CODE / CONFIG

3. Use save_triage_report to save it""",
    model=model,
    tools=[read_tool, save_tool],
)

# ── UI ────────────────────────────────────────────────────
run_adk_web(
    agents_list=[triage_agent],
    port=8000,
    agent_env="local",
)
```

Stop, replace, run again. Then type:
```
triage the log file: sample_failure.log
