from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from .fact_check import fact_check_claim

app = FastAPI(title="kant lAi Fact Checker")

class ClaimRequest(BaseModel):
    claim: str

@app.post("/fact_check")
async def fact_check_endpoint(request: ClaimRequest):
    try:
        result = await fact_check_claim(request.claim)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
