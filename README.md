CantL-AI — AI-Powered Fact-Checking Assistant

«Question misinformation. Verify claims. Make informed decisions.»

CantL-AI is an open-source, multi-platform fact-checking assistant designed to help users evaluate the credibility of information encountered online. By combining web search, domain-trust scoring, and AI-assisted analysis, it provides a truthfulness estimate and confidence score to help users assess potentially misleading claims.

With a browser extension, a FastAPI backend, and an Android application, CantL-AI brings fact-checking closer to where people consume information.

✨ Features

- One-Click Fact-Checking: Evaluate claims from social media platforms, including Instagram, Facebook, and X (formerly Twitter).
- AI-Assisted Analysis: Integrates Google Gemini for claim evaluation when an API key is configured.
- Web-Based Verification: Uses Bing web search to retrieve relevant information for evaluating claims.
- Domain Trust Scoring: Considers domain credibility as part of the verification process.
- Truthfulness Estimation: Returns a truthfulness probability to help users interpret the assessment.
- Cross-Platform Support: Provides a Chrome/Edge browser extension and an Android application.
- Modern User Interface: Features a glassmorphism-inspired browser extension with a clean, pastel-themed design.
- Fallback Analysis: Supports deterministic fallback scoring when Gemini integration is unavailable.
- FastAPI Backend: Provides a centralized backend for coordinating fact-checking operations.
- Docker Support: Includes a Dockerfile to simplify backend deployment.

🧠 How It Works

CantL-AI follows a straightforward fact-checking workflow:

flowchart TD
    A[User encounters a claim] --> B{Choose platform}
    B --> C[Chrome / Edge Extension]
    B --> D[Android Application]
    C --> E[FastAPI Backend]
    D --> E
    E --> F[Web Search using Bing]
    E --> G[Domain Trust Scoring]
    F --> H[Claim Evaluation]
    G --> H
    H --> I{Gemini Available?}
    I -->|Yes| J[AI-Assisted Analysis]
    I -->|No| K[Deterministic Fallback]
    J --> L[Truthfulness Estimate]
    K --> L
    L --> M[Display Verdict and Confidence]

Verification workflow

1. Capture: The user initiates a fact-check from the browser extension or Android application.
2. Process: The backend receives the claim for analysis.
3. Retrieve: Bing web search helps gather relevant information when configured correctly.
4. Assess: Domain-trust scoring and the available analysis mechanism contribute to the assessment.
5. Respond: The backend returns the result for presentation in the client interface.

Note: A truthfulness probability is an estimate, not proof that a claim is true or false. Results depend on the available evidence, configuration, and analysis method.

🏗️ Project Architecture

The repository is organized into three primary components:

CantL-AI/
├── android/                 # Android application
├── backend/
│   └── app/                 # FastAPI backend and application logic
├── extension/               # Chrome/Edge browser extension
├── .vscode/                 # Editor configuration
├── Dockerfile               # Backend container configuration
├── requirements.txt         # Python dependencies
└── README.md                # Project documentation

Components

Component| Responsibility
Browser Extension| Provides an interface for initiating fact-checks from supported social media platforms.
FastAPI Backend| Coordinates incoming requests and fact-checking operations.
Bing Web Search| Retrieves relevant web information when configured.
Domain Trust Scoring| Incorporates source-domain credibility into the assessment.
Gemini Integration| Enables optional AI-assisted claim evaluation.
Fallback Scorer| Provides an alternative scoring path when Gemini is not configured.
Android Application| Offers a mobile interface through a Floating Action Button (FAB).

🛠️ Tech Stack

- Backend: Python, FastAPI
- API Server: Uvicorn
- AI Integration: Google Gemini API
- Web Search: Bing Web Search API
- Frontend: HTML, CSS, JavaScript
- Browser Support: Chrome and Microsoft Edge
- Mobile: Android
- Deployment: Docker

🚀 Getting Started

Follow these instructions to run CantL-AI locally.

Prerequisites

Install the following tools before starting:

- Python 3.10 or a compatible version supported by the project dependencies
- pip
- Google Chrome or Microsoft Edge
- Android Studio, if you want to run the mobile application
- Docker, if you prefer containerized backend deployment

You will also need API credentials for the external services you want to use. The Gemini integration is optional; Bing search requires a valid API key for live search functionality.

1. Clone the repository

git clone https://github.com/anu-rag-007/CantL-AI.git
cd CantL-AI

2. Set up the Python backend

Create and activate a virtual environment.

Windows — PowerShell

python -m venv venv
.\venv\Scripts\Activate.ps1

Windows — Command Prompt

python -m venv venv
venv\Scripts\activate

Linux / macOS

python3 -m venv venv
source venv/bin/activate

Install the dependencies:

pip install -r requirements.txt

3. Configure API keys

Set the Gemini API key as an environment variable in the terminal where you will run the backend.

Windows — PowerShell

$env:GEMINI_API_KEY="your_gemini_api_key"

Windows — Command Prompt

set GEMINI_API_KEY=your_gemini_api_key

Linux / macOS

export GEMINI_API_KEY="your_gemini_api_key"

Obtain a Gemini API key from "Google AI Studio" (https://aistudio.google.com/).

For Bing search, open:

backend/app/search_api.py

Replace the "YOUR_BING_API_KEY" placeholder with a valid Bing API key supported by the implementation.

Security reminder: Never commit API keys, access tokens, or other credentials to GitHub. Use environment variables or an appropriately configured secrets manager for sensitive values.

4. Start the backend

From the repository root, run:

uvicorn backend.app.main:app --reload

The backend should be available at:

- API base URL: http://127.0.0.1:8000
- Interactive API documentation: http://127.0.0.1:8000/docs

The documentation page can be used to inspect available endpoints and test supported API requests.

5. Load the browser extension

1. Open Chrome or Microsoft Edge.
2. Navigate to the browser's extensions management page.
   - Chrome: "chrome://extensions"
   - Edge: "edge://extensions"
3. Enable Developer mode.
4. Select Load unpacked.
5. Choose the repository's "extension/" directory.
6. Open a supported social media page and try the extension's fact-checking interface.

Ensure the extension's backend URL points to the running FastAPI server. If the project uses a different configuration file or API URL, update the existing configuration accordingly.

6. Run the Android application

1. Open Android Studio.
2. Select File → Open.
3. Choose the repository's "android/" directory.
4. Allow Gradle to synchronize the project.
5. Start an Android emulator or connect a compatible device.
6. Run the application.

For an emulator or physical device, configure the backend address appropriately. An Android emulator commonly uses "10.0.2.2" to access the host machine's localhost; a physical device generally needs the host machine's reachable local network address.

Make sure the backend is running and reachable before testing the app.

🐳 Run with Docker

The repository includes a Dockerfile for backend deployment.

Build the image from the repository root:

docker build -t cantl-ai .

Start the container:

docker run --rm -p 8000:8000 \
  -e GEMINI_API_KEY="your_gemini_api_key" \
  cantl-ai

The exact container startup command and port configuration depend on the included Dockerfile. Configure the Bing API key using the mechanism supported by the current implementation.

For production deployments, avoid placing credentials directly in shell history or deployment files. Use a secure secrets mechanism instead.

⚙️ Configuration

Setting| Purpose
"GEMINI_API_KEY"| Enables the optional Gemini integration.
Bing API key| Enables web-search functionality through the configured search implementation.
Backend URL| Connects the browser extension and Android app to the FastAPI server.

If the Gemini API key is unavailable, the backend can use its fallback scorer. If Bing search is not configured, search results may be empty, limiting the available evidence.

🧪 Testing the Application

After starting the backend and connecting a client, test the following scenarios:

- Submit a factual claim and inspect the returned assessment.
- Test a claim with relevant supporting web sources.
- Test a claim with contradictory or insufficient evidence.
- Verify behavior when Gemini is not configured.
- Verify behavior when Bing search returns no results.
- Check that the browser extension and Android application can reach the backend.
- Test invalid requests and network failures.

Evaluate the results against known evidence rather than relying only on the displayed confidence percentage.

🔐 Responsible Use and Limitations

CantL-AI is intended to assist users in evaluating online information. It should not be treated as an authoritative source of truth.

- No guaranteed accuracy: AI-generated assessments and numerical confidence scores can be incorrect.
- Evidence quality matters: Search results may be incomplete, outdated, biased, or unrelated to the claim.
- Source credibility is contextual: A trusted domain can publish incorrect information, and a lesser-known domain can publish accurate information.
- Fallback limitations: Deterministic fallback scoring is not equivalent to a fully capable AI reasoning system.
- Privacy considerations: Avoid submitting sensitive personal information or confidential material unless the system's data handling has been reviewed.
- API availability: External services may impose rate limits, usage restrictions, or other access requirements.

Always consult reliable primary sources for high-impact decisions.

🗺️ Future Improvements

Potential areas for future development include:

- [ ] Evidence-linked verdicts with clickable source references.
- [ ] Clear separation between verified facts, disputed claims, and insufficient evidence.
- [ ] Improved multilingual fact-checking.
- [ ] Transparent scoring explanations and evidence quality indicators.
- [ ] Expanded automated testing and evaluation against curated datasets.
- [ ] Stronger privacy controls and secure API configuration.
- [ ] Improved handling of conflicting sources and breaking news.
- [ ] More robust browser-extension and Android error handling.
- [ ] Production-ready deployment, monitoring, and rate limiting.

🤝 Contributing

Contributions, bug reports, suggestions, and improvements are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes and test them locally.
4. Commit your changes with a descriptive message.
5. Open a pull request describing the changes.

For significant changes, consider opening an issue first to discuss the proposed approach.

📄 License

No license has been specified in this README. Check the repository for an existing license before redistributing or reusing the code. If you intend to publish CantL-AI as an open-source project, consider adding an appropriate "LICENSE" file.

👨‍💻 Author

Anurag Sharma

- GitHub: "@anu-rag-007" (https://github.com/anu-rag-007)
- Project Repository: "CantL-AI" (https://github.com/anu-rag-007/CantL-AI)

---

<p align="center">
  <strong>CantL-AI</strong><br>
  Think critically. Check the evidence. Share responsibly.
</p>
