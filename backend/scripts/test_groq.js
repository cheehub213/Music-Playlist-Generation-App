require("../src/config/env").loadEnv();

const { getGroqConfig } = require("../src/config/groq");

function redact(value) {
  if (!value) {
    return "";
  }
  if (value.length <= 8) {
    return "***";
  }
  return `${value.slice(0, 3)}***${value.slice(-2)}`;
}

async function readJson(response) {
  const text = await response.text();
  try {
    return JSON.parse(text);
  } catch (err) {
    return { raw: text };
  }
}

async function main() {
  const config = getGroqConfig();

  console.log("Groq config:", {
    hasKey: Boolean(config.apiKey),
    key: redact(config.apiKey),
    baseUrl: config.baseUrl,
    apiUrl: config.apiUrl,
    model: config.model,
    timeoutMs: config.timeoutMs,
    maxRetries: config.maxRetries,
  });

  if (!config.apiKey) {
    throw new Error("Missing GROQ_API_KEY in .env.local");
  }

  const modelsResponse = await fetch(config.modelsUrl, {
    headers: { Authorization: `Bearer ${config.apiKey}` },
  });
  const modelsBody = await readJson(modelsResponse);

  if (!modelsResponse.ok) {
    throw new Error(
      `Model-list auth check failed: ${modelsResponse.status} ${modelsBody.error?.message || modelsBody.error || modelsBody.raw || ""}`,
    );
  }

  console.log("Model-list auth check: ok", {
    status: modelsResponse.status,
    modelCount: Array.isArray(modelsBody.data) ? modelsBody.data.length : null,
  });

  const chatResponse = await fetch(config.apiUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${config.apiKey}`,
    },
    body: JSON.stringify({
      model: config.model,
      temperature: 0,
      max_tokens: 16,
      messages: [
        { role: "system", content: "Reply with exactly: ok" },
        { role: "user", content: "Health check" },
      ],
    }),
  });
  const chatBody = await readJson(chatResponse);

  if (!chatResponse.ok) {
    throw new Error(
      `Chat completion check failed: ${chatResponse.status} ${chatBody.error?.message || chatBody.error || chatBody.raw || ""}`,
    );
  }

  const content = chatBody.choices?.[0]?.message?.content;
  console.log("Chat completion check: ok", {
    status: chatResponse.status,
    responsePreview: typeof content === "string" ? content.slice(0, 40) : "",
  });
}

main().catch((err) => {
  console.error("Groq check failed:", err.message);
  process.exitCode = 1;
});
