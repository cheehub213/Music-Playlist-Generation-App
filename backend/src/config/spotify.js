function parseIntOrDefault(value, defaultValue) {
  const parsed = Number.parseInt(String(value || ""), 10);
  return Number.isFinite(parsed) ? parsed : defaultValue;
}

function normalizeBaseUrl(value) {
  return String(value || "https://api.spotify.com/v1").replace(/\/+$/, "");
}

function getSpotifyConfig() {
  const baseUrl = normalizeBaseUrl(process.env.SPOTIFY_API_BASE_URL);
  const fallbackTimeoutMs = parseIntOrDefault(process.env.SPOTIFY_TIMEOUT_MS, 120000);
  return {
    clientId: process.env.SPOTIFY_CLIENT_ID || "",
    clientSecret: process.env.SPOTIFY_CLIENT_SECRET || "",
    tokenUrl: process.env.SPOTIFY_TOKEN_URL || "https://accounts.spotify.com/api/token",
    baseUrl,
    searchUrl: process.env.SPOTIFY_SEARCH_URL || `${baseUrl}/search`,
    market: process.env.SPOTIFY_MARKET || process.env.SPOTIFY_COUNTRY || "US",
    country: process.env.SPOTIFY_COUNTRY || process.env.SPOTIFY_MARKET || "US",
    timeoutMs: fallbackTimeoutMs,
    connectTimeoutMs: parseIntOrDefault(process.env.SPOTIFY_CONNECT_TIMEOUT_MS, 5000),
    writeTimeoutMs: parseIntOrDefault(process.env.SPOTIFY_WRITE_TIMEOUT_MS, 30000),
    readTimeoutMs: parseIntOrDefault(process.env.SPOTIFY_READ_TIMEOUT_MS, fallbackTimeoutMs),
  };
}

module.exports = { getSpotifyConfig };
