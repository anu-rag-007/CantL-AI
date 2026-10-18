import re
from urllib.parse import urlparse

# Simple trust list of known reputable domains (can be expanded)
TRUSTED_DOMAINS = {
    "bbc.co.uk",
    "nytimes.com",
    "theguardian.com",
    "reuters.com",
    "apnews.com",
    "washingtonpost.com",
    "nature.com",
    "sciencemag.org",
}

def assess_trustworthiness(snippets):
    """Assess the average trust score of the domains from which snippets were collected.
    Returns a value between 0 (untrusted) and 1 (highly trusted).
    """
    if not snippets:
        return 0.0
    # For this demo, we assume each snippet is a string possibly containing a URL.
    # We'll extract URLs using a regex and compute trust based on domain.
    url_regex = re.compile(r"https?://[\w./?=&%-]+")
    scores = []
    for snippet in snippets:
        urls = url_regex.findall(snippet)
        if not urls:
            continue
        for u in urls:
            domain = urlparse(u).netloc.lower()
            # Strip subdomains
            parts = domain.split('.')
            if len(parts) > 2:
                domain = '.'.join(parts[-2:])
            scores.append(1.0 if domain in TRUSTED_DOMAINS else 0.0)
    if not scores:
        return 0.0
    return sum(scores) / len(scores)
