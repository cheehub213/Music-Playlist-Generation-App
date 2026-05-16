function parseIntOrDefault(value, defaultValue) {
  const parsed = Number.parseInt(String(value || ""), 10);
  return Number.isFinite(parsed) ? parsed : defaultValue;
}

function normalizeBaseUrl(value) {
  return String(value || "https://api.groq.com/openai/v1").replace(/\/+$/, "");
}

function getGroqConfig() {
  const baseUrl = normalizeBaseUrl(process.env.GROQ_BASE_URL || process.env.GROQ_API_BASE_URL);
  const fallbackTimeoutMs = parseIntOrDefault(process.env.GROQ_TIMEOUT_MS, 120000);
  return {
    apiKey: process.env.GROQ_API_KEY || "",
    baseUrl,
    apiUrl: process.env.GROQ_API_URL || `${baseUrl}/chat/completions`,
    modelsUrl: `${baseUrl}/models`,
    model: process.env.GROQ_MODEL || "llama-3.3-70b-versatile",
    timeoutMs: fallbackTimeoutMs,
    connectTimeoutMs: parseIntOrDefault(process.env.GROQ_CONNECT_TIMEOUT_MS, 2000),
    writeTimeoutMs: parseIntOrDefault(process.env.GROQ_WRITE_TIMEOUT_MS, 30000),
    readTimeoutMs: parseIntOrDefault(process.env.GROQ_READ_TIMEOUT_MS, fallbackTimeoutMs),
    maxRetries: parseIntOrDefault(process.env.GROQ_MAX_RETRIES, 2),
  };
}

module.exports = { getGroqConfig };
