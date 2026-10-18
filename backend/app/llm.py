import os
import json
import google.generativeai as genai

# Initialise the Gemini client once
api_key = os.getenv("GEMINI_API_KEY")
if not api_key:
    raise RuntimeError("GEMINI_API_KEY environment variable not set")
genai.configure(api_key=api_key)

model = genai.GenerativeModel("gemini-3.1-flash-lite")  

def evaluate_claim(claim: str, snippets: list[str]) -> float:
    """
    Calls the Gemini model to assess the truthfulness of *claim* given the
    collected *snippets* (search results).

    Returns a probability between 0 and 1 where higher means more likely true.
    """
    # Build a prompt that asks the model to judge the claim.
    prompt = (
        "You are a fact‑checking assistant. "
        "Given the following evidence snippets, decide whether the claim is true, false, or mixed. "
        "Return ONLY a single number between 0 (definitely false) and 1 (definitely true). "
        "\n\n"
        f"Claim: {claim}\n\n"
        "Evidence:\n" + "\n".join(f"- {s}" for s in snippets[:5])  # limit length
        + "\n\nAnswer:"
    )

    # Call Gemini (blocking; can be wrapped in a thread if you prefer async)
    response = model.generate_content(prompt)

    # The model returns text; try to extract a float.
    try:
        # Strip any surrounding whitespace / newlines
        text = response.text.strip()
        # Keep only the first number we can parse
        score = float(text.split()[0])
        # Clamp to [0, 1]
        return max(0.0, min(1.0, score))
    except Exception as e:
        # If parsing fails, fall back to a neutral score
        print(f"Gemini parsing error: {e!r}. Raw response: {response.text!r}")
        return 0.5