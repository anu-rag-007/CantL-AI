// content_script.js – extracts post text from supported social‑media pages
chrome.runtime.onMessage.addListener((msg, sender, sendResponse) => {
  if (msg.action === 'getPostText') {
    let postText = '';
    const selectors = [
      '[role="heading"]',            // Twitter/X tweet heading
      'article',                      // generic article container
      '[data-testid="tweetText"]',   // Twitter tweet text
      '.userContent',                 // Facebook post text
      '.C7I1f'                        // Instagram caption class (may vary)
    ];
    for (const sel of selectors) {
      const el = document.querySelector(sel);
      if (el && el.innerText.trim()) {
        postText = el.innerText.trim();
        break;
      }
    }
    sendResponse({text: postText});
    return true; // keep channel open for async response
  }
});
