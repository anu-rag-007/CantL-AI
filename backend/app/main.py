from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from .fact_check import fact_check_claim
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(title="CantL-AI")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"], # For production, replace "*" with your extension's ID
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ClaimRequest(BaseModel):
    claim: str

@app.post("/fact_check")
async def fact_check_endpoint(request: ClaimRequest):
    try:
        result = await fact_check_claim(request.claim)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
