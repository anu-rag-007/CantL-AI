// background.js – runs as a service worker
chrome.action.onClicked.addListener((tab) => {
  if (!tab.id) return;
  chrome.tabs.sendMessage(tab.id, {action: 'getPostText'}, (response) => {
    const text = response?.text ?? '';
    // Store the extracted text so popup can read it later
    chrome.storage.local.set({postText: text});
    // Open the popup (handled automatically by manifest default_popup)
  });
});
