# backend/app/search_api.py
import os
import requests

def _get_key() -> str | None:
    """Read the Bing API key from the environment, or return None."""
    return os.getenv("BING_API_KEY")   # may be None or empty

def search_web(query: str, top_n: int = 5):
    """
    Perform a Bing Web Search (free tier) **if** a valid API key is available.
    If no key is set, returns an empty list so the pipeline can still run.
    """
    api_key = _get_key()
    if not api_key:
        # No key – graceful fallback
        return []   # empty snippet list

    endpoint = "https://api.bing.microsoft.com/v7.0/search"
    headers = {"Ocp-Apim-Subscription-Key": api_key}
    params = {"q": query, "count": top_n}
    resp = requests.get(endpoint, headers=headers, params=params, timeout=10)
    resp.raise_for_status()   # will raise 401/403 if the key is wrong
    data = resp.json()

    snippets = []
    for page in data.get("webPages", {}).get("value", []):
        snippet = page.get("snippet")
        if snippet:
            snippets.append(snippet)
    return snippets

    try:
        resp = requests.get(endpoint, headers=headers, params=params, timeout=10)
        resp.raise_for_status()
        data = resp.json()
    except requests.exceptions.RequestException as e:
        print(f"Bing Search failed: {e}")
        return [] # Return empty so the pipeline continues
