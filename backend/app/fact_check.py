import asyncio
from .search_api import search_web
from .credibility import assess_trustworthiness
from .llm import evaluate_claim

async def fact_check_claim(claim: str):
    """Orchestrates the fact‑checking pipeline.

    1. Perform a web search (Bing) for the claim and collect snippets.
    2. Compute a trust score for the domains of the top results.
    3. Use a HuggingFace LLM to evaluate the claim against the snippets.
    4. Combine the trust and LLM scores into a final truthfulness probability.
    """
    # 1. Search the web (async wrapper)
    snippets = await asyncio.to_thread(search_web, claim)
    # 2. Domain trust assessment (average trust score)
    trust_score = assess_trustworthiness(snippets)
    # 3. LLM evaluation (returns a probability between 0 and 1)
    llm_score = await asyncio.to_thread(evaluate_claim, claim, snippets)
    # 4. Simple weighted average (adjust weights as needed)
    truthfulness = 0.6 * llm_score + 0.4 * trust_score
    return {"truthfulness": truthfulness, "llm_score": llm_score, "trust_score": trust_score, "snippets": snippets}
