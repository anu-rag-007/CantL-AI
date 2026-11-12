// popup.js

// 1. Auto-grab selected text when the popup opens
document.addEventListener('DOMContentLoaded', async () => {
  // Get the active tab
  let [tab] = await chrome.tabs.query({ active: true, currentWindow: true });

  if (tab) {
    // Inject a script into the page to get the selected text
    chrome.scripting.executeScript({
      target: { tabId: tab.id },
      function: getSelectionText,
    }, (injectionResults) => {
      // If text was found, put it in the textarea
      if (injectionResults && injectionResults[0].result) {
        document.getElementById('claimInput').value = injectionResults[0].result;
      }
    });
  }
});

// This function runs inside the webpage's context
function getSelectionText() {
  return window.getSelection().toString();
}

// 2. Your existing click listener (keep this below the new code)
document.getElementById('checkBtn').addEventListener('click', async () => {
    // ... (keep your existing fetch logic here)
    const claim = document.getElementById('claimInput').value;
    const resultBox = document.getElementById('resultBox');
    const scoreDisplay = document.getElementById('scoreDisplay');
    const btn = document.getElementById('checkBtn');
  
    if (!claim) return alert("Please enter a claim.");
  
    btn.innerText = "Checking...";
    resultBox.classList.add('hidden');
  
    try {
      const response = await fetch("http://127.0.0.1:8000/fact_check", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ claim: claim })
      });
  
      const data = await response.json();
      const percentage = (data.truthfulness * 100).toFixed(1);
      
      scoreDisplay.innerText = `${percentage}% Truthful`;
      resultBox.classList.remove('hidden');
      
      if (percentage < 30) scoreDisplay.style.color = "#e74c3c"; // Red
      else if (percentage < 70) scoreDisplay.style.color = "#f1c40f"; // Yellow
      else scoreDisplay.style.color = "#2ecc71"; // Green
  
    } catch (error) {
      console.error("Error:", error);
      alert("Failed to connect to the backend. Is uvicorn running?");
    } finally {
      btn.innerText = "Fact Check";
    }
});