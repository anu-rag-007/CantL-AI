import os
from dotenv import load_dotenv
from google import genai
from google.genai import types

# Load environment variables
load_dotenv()

# Initialize the new, unified client
api_key = os.getenv("GEMINI_API_KEY")
client = None

if api_key:
    # The new SDK automatically picks up the key if passed here
    client = genai.Client(api_key=api_key)
    print("✅ GenAI Client initialized successfully.")
else:
    print("❌ WARNING: Could not find GEMINI_API_KEY in the environment.")


def evaluate_claim(claim: str, snippets: list[str]) -> float:
    """
    Calls the Gemini model using the new GenAI SDK.
    If no client is initialized, returns a dummy score of 0.5.
    """
    if not client:
        print("⚠️ No GEMINI_API_KEY found. Using dummy scorer (0.5).")
        return 0.5

    prompt = (
        "You are a fact‑checking assistant. "
        "Given the following evidence snippets, decide whether the claim is true, false, or mixed. "
        "Return ONLY a single number between 0 (definitely false) and 1 (definitely true). "
        "\n\n"
        f"Claim: {claim}\n\n"
        "Evidence:\n" + "\n".join(f"- {s}" for s in snippets[:5])
        + "\n\nAnswer:"
    )

    try:
        # The new SDK uses client.models.generate_content
        response = client.models.generate_content(
            model='gemini-3.8-flash',  # A modern, fast model
            contents=prompt,
            # Use types.GenerateContentConfig for advanced settings if needed
            config=types.GenerateContentConfig(
                temperature=0.0,  # Low temperature for more deterministic answers
            )
        )
        
        # Extract the text from the response
        text = response.text.strip()
        
        # Parse the first number from the text
        score = float(text.split()[0])
        
        # Clamp the score to ensure it's between 0 and 1
        return max(0.0, min(1.0, score))
        
    except Exception as e:
        print(f"Gemini parsing error: {e!r}")
        return 0.5