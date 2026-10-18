# CantL‑AI

CantL‑AI is an open‑source, multi‑platform fact‑checking assistant that works as:

- **Chrome/Edge extension** with a modern glass‑morphism UI.
- **FastAPI backend** that orchestrates a Bing web search, domain‑trust scoring, and a Gemini LLM (or a deterministic fallback) to return a truthfulness probability.
- **Minimal Android app** that calls the same backend via a Floating Action Button.

## Features
- One‑click fact‑check from Instagram, Facebook, and X/Twitter.
- Free‑tier **Bing** web search (replace the placeholder key for production).
- **Google Gemini** LLM integration (optional – works without a key using a dummy scorer).
- Glass‑morphism extension UI (soft pastel colors, blurred background, rounded corners).
- Dockerfile for easy deployment of the backend.

## Quick start
1. **Open the project folder** `d:/Practice set/CantL-AI`.
2. **Backend** – create a virtual environment, install dependencies, and run the server:
   ```powershell
   cd "d:/Practice set/CantL-AI"
   python -m venv venv
   .\venv\Scripts\activate
   pip install -r requirements.txt
   uvicorn backend.app.main:app --reload
   ```
   The API will be available at `http://127.0.0.1:8000`.
3. **Extension** – open Chrome/Edge → Extensions → Developer mode → Load unpacked → select `extension` folder.
4. **Android** – open Android Studio, `File → Open`, choose the `android` folder, let Gradle sync, then run on an emulator.
5. **Test** – click the extension button on a social‑media post or tap the FAB in the Android app. The UI will show a verdict and confidence percentage.

## Configuration
- **Gemini API key** – set an environment variable `GEMINI_API_KEY` in the same shell that runs `uvicorn`.
- **Bing API key** – edit `backend/app/search_api.py` and replace `YOUR_BING_API_KEY` with a valid key (optional; without it the search returns an empty list).

Enjoy exploring reliable information with CantL‑AI!
