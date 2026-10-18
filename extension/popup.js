// popup.js – Handles UI interaction and calls the FastAPI backend

// When the button is clicked, retrieve the post text stored by the content script
// and send it to the backend for fact‑checking.

document.getElementById('checkBtn').addEventListener('click', async () => {
  // Retrieve post text from the content script via messaging
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (!tab.id) return;

  const postText = await new Promise((resolve) => {
    chrome.tabs.sendMessage(tab.id, { action: 'getPostText' }, (response) => {
      resolve(response?.text ?? '');
    });
  });

  if (!postText) {
    showResult('⚠️ Could not extract post text.');
    return;
  }

  try {
    const resp = await fetch('http://127.0.0.1:8000/fact_check', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ claim: postText })
    });
    const data = await resp.json();
    const score = (data.truthfulness * 100).toFixed(1);
    const verdict = data.truthfulness > 0.66 ? '✅ Likely True' : data.truthfulness > 0.33 ? '🤔 Mixed' : '❌ Likely False';
    showResult(`${verdict} – Confidence: ${score}%`);
  } catch (e) {
    console.error(e);
    showResult('❌ Error contacting the backend.');
  }
});

function showResult(message) {
  const resultDiv = document.getElementById('result');
  resultDiv.textContent = message;
}
