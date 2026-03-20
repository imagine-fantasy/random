import os
import json
import getpass
import requests
import datetime
from pathlib import Path
from smart_sdk.agents import LLMAgent
from smart_sdk.models import Model, ModelProvider
from smart_sdk.tools import Tool
from smart_sdk.cli import run_adk_web

# ── Sandbox Auth ──────────────────────────────────────────
PRD_CLIENT_ID = "PC-86862-SID-27876-PROD"
PRD_SCOPE = "JPMC:URI:RS-86862-25458-cblab-PROD"
PRD_IDA = "https://idag2.jpmorganchase.com/adfs/oauth2/token"
TOKEN_FILE = Path.home() / ".llm_sandbox_token.json"

def login():
    # Use cached token if valid
    if TOKEN_FILE.exists():
        try:
            with open(TOKEN_FILE, "r") as f:
                token_data = json.load(f)
            expiry = datetime.datetime.fromisoformat(token_data["expiry"])
            if datetime.datetime.now() < expiry:
                os.environ["AZURE_OPENAI_API_KEY"] = token_data["token"]
                print("Using cached token.")
                return token_data["token"]
        except:
            pass

    print("\n=== Login to LLM Sandbox ===\n")
    username = input("Enter username (e.g. NAEAST\\R123457): ")
    password = getpass.getpass("Enter password: ")

    response = requests.post(PRD_IDA, data={
        "grant_type": "password",
        "client_id": PRD_CLIENT_ID,
        "username": username,
        "password": password,
        "resource": PRD_SCOPE
    })

    token = response.json().get("access_token")
    expires_in = int(response.json().get("expires_in", 3600))
    expiry = datetime.datetime.now() + datetime.timedelta(seconds=expires_in)

    with open(TOKEN_FILE, "w") as f:
        json.dump({"token": token, "expiry": expiry.isoformat()}, f)

    os.environ["AZURE_OPENAI_API_KEY"] = token
    print("\nLogin successful!\n")
    return token

# ── Login first ───────────────────────────────────────────
token = login()

# ── Model via Sandbox ─────────────────────────────────────
model = Model(
    name="gpt-4.1-2025-04-14",
    provider=ModelProvider.AZURE_OPENAI,
    azure_deployment_name="gpt-4.1-2025-04-14",
    azure_endpoint="https://service.cbcdo.dev.aws.jpmchase.net/ver2/",
    azure_api_version="2024-10-21",
    api_key=os.environ["AZURE_OPENAI_API_KEY"],
    auth_token=os.environ["AZURE_OPENAI_API_KEY"],
)

# ── Tools ─────────────────────────────────────────────────
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

read_tool = Tool(tool_name="read_log_file", func=read_log_file)
save_tool = Tool(tool_name="save_triage_report", func=save_triage_report)

# ── Agent ─────────────────────────────────────────────────
triage_agent = LLMAgent(
    name="LogTriageExpert",
    description="Triages Spinnaker and Jules CI/CD failures",
    system_message="""You are a senior DevOps engineer specialising in
Spinnaker and Jules CI/CD pipeline failures at a large bank.

You have two tools:
- read_log_file: reads a log file from disk
- save_triage_report: saves your analysis

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
