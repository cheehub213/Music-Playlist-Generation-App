const { getGroqConfig } = require("../config/groq");
const { AppError } = require("../utils/errors");
const logger = require("../utils/logger");

function sleep(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function extractJson(text) {
  if (typeof text !== "string") {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch (err) {
    const start = text.indexOf("{");
    const end = text.lastIndexOf("}");
    if (start >= 0 && end > start) {
      try {
        return JSON.parse(text.slice(start, end + 1));
      } catch (nestedErr) {
        return null;
      }
    }
    return null;
  }
}

function contentFromResponse(data) {
  const content = data?.choices?.[0]?.message?.content;
  if (typeof content === "string") {
    return content;
  }
  if (Array.isArray(content)) {
    const textPart = content.find((item) => typeof item?.text === "string");
    return textPart?.text || "";
  }
  return "";
}

function shouldRetry(statusCode) {
  return statusCode === 429 || statusCode >= 500;
}

function getNetworkErrorCode(err) {
  return err?.code || err?.cause?.code || "";
}

function isConnectionError(err) {
  const code = getNetworkErrorCode(err);
  return code === "ENOTFOUND"
    || code === "ECONNREFUSED"
    || code === "EAI_AGAIN"
    || /connect/i.test(String(err?.message || ""));
}

function toServiceUnreachableError(serviceName, err) {
  const code = getNetworkErrorCode(err);
  const codeSuffix = code ? ` (${code})` : "";
  return new AppError(`${serviceName} service unreachable${codeSuffix}. Check network or base URL.`, 503);
}

function errorMessageFromResponse(data, status) {
  return data?.error?.message || data?.error || `Groq API failed with status ${status}`;
}

async function requestStructuredCompletion({ systemPrompt, userPrompt, schemaHint }) {
  const config = getGroqConfig();
  if (!config.apiKey) {
    throw new AppError("Missing GROQ_API_KEY in environment", 500);
  }

  const payloadBase = {
    model: config.model,
    temperature: 0.2,
    response_format: { type: "json_object" },
    messages: [
      { role: "system", content: systemPrompt },
      {
        role: "user",
        content: [
          userPrompt,
          "Return strictly valid JSON.",
          schemaHint,
        ].join("\n\n"),
      },
    ],
  };

  const attempts = Math.max(0, config.maxRetries) + 1;
  let lastError;

  for (let attempt = 1; attempt <= attempts; attempt += 1) {
    const controller = new AbortController();
    const timedOutStage = { value: null };
    const connectTimeoutId = setTimeout(() => {
      timedOutStage.value = "connect";
      controller.abort();
    }, config.connectTimeoutMs);
    const writeTimeoutId = setTimeout(() => {
      timedOutStage.value = "write";
      controller.abort();
    }, config.writeTimeoutMs);

    try {
      const response = await fetch(config.apiUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${config.apiKey}`,
        },
        body: JSON.stringify(payloadBase),
        signal: controller.signal,
      });

      clearTimeout(connectTimeoutId);
      clearTimeout(writeTimeoutId);
      const readTimeoutId = setTimeout(() => {
        timedOutStage.value = "read";
        controller.abort();
      }, config.readTimeoutMs);

      let responseText = "";
      try {
        responseText = await response.text();
      } finally {
        clearTimeout(readTimeoutId);
      }
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (err) {
        data = { raw: responseText };
      }

      if (!response.ok) {
        const status = response.status;
        const message = errorMessageFromResponse(data, status);
        const retriable = shouldRetry(status);
        logger.warn("Groq request returned non-2xx response", {
          status,
          attempt,
          retriable,
          promptPreview: String(userPrompt).slice(0, 120),
          responseTextPreview: String(responseText).slice(0, 500),
          modelAttempted: config.model,
        });

        if (retriable && attempt < attempts) {
          await sleep(300 * attempt);
          continue;
        }
        throw new AppError(message, status);
      }

      const content = contentFromResponse(data);
      const parsed = extractJson(content);
      if (!parsed) {
        throw new AppError("Groq returned an invalid JSON payload", 502);
      }

      return parsed;
    } catch (err) {
      let nextError = err;
      if (err?.name === "AbortError" && timedOutStage.value) {
        nextError = new AppError(`Groq request timed out during ${timedOutStage.value}`, 504);
      } else if (!(err instanceof AppError) && isConnectionError(err)) {
        nextError = toServiceUnreachableError("Groq", err);
      }

      const timedOut = nextError?.status === 504 || nextError?.name === "AbortError";
      const retriable = timedOut || (nextError?.status >= 500 && nextError?.status !== 503);

      logger.warn("Groq request failed", {
        attempt,
        retriable,
        timedOut,
        error: nextError.message,
        prompt: userPrompt,
      });

      lastError = nextError;
      if (!retriable || attempt >= attempts) {
        break;
      }
      await sleep(350 * attempt);
    } finally {
      clearTimeout(connectTimeoutId);
      clearTimeout(writeTimeoutId);
    }
  }

  if (lastError instanceof AppError) {
    throw lastError;
  }
  throw new AppError(lastError?.message || "Groq request failed", 502);
}

async function generateSongRecommendations(moodPrompt) {
  const systemPrompt = `You are AuraBeat's DJ curator. Given a music mood or vibe description, return ONLY a valid JSON array of exactly 10 songs. Each song must have "title" and "artist" fields. Return ONLY the JSON array, no other text.`;

  const userMessage = `Curate 10 songs for this vibe: ${moodPrompt}. Return ONLY valid JSON array like this: [{"title":"Song Name","artist":"Artist Name"}]`;
  
  const config = getGroqConfig();
  if (!config.apiKey) {
    throw new AppError("Missing GROQ_API_KEY in environment", 500);
  }

  const payload = {
    model: config.model,
    messages: [
      { role: "system", content: systemPrompt },
      { role: "user", content: userMessage }
    ],
    temperature: 0.7,
    max_tokens: 2000
  };

  const controller = new AbortController();
  const timedOutStage = { value: null };
  const connectTimeoutId = setTimeout(() => {
    timedOutStage.value = "connect";
    controller.abort();
  }, config.connectTimeoutMs);

  const writeTimeoutId = setTimeout(() => {
    timedOutStage.value = "write";
    controller.abort();
  }, config.writeTimeoutMs);

  try {
    const response = await fetch(config.apiUrl, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${config.apiKey}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload),
      signal: controller.signal,
    });

    clearTimeout(connectTimeoutId);
    clearTimeout(writeTimeoutId);

    const readTimeoutId = setTimeout(() => {
      timedOutStage.value = "read";
      controller.abort();
    }, config.readTimeoutMs);

    let text = "";
    try {
      text = await response.text();
    } finally {
      clearTimeout(readTimeoutId);
    }

    let data;
    try {
      data = JSON.parse(text);
    } catch (err) {
      data = { raw: text };
    }

    if (!response.ok) {
      const message = errorMessageFromResponse(data, response.status);
      logger.warn("Groq request failed", { status: response.status, message });
      throw new AppError(message, response.status);
    }

    // Extract content from response
    const content = contentFromResponse(data);
    
    // Try to parse as JSON
    try {
      const jsonMatch = content.match(/\[[\s\S]*\]/);
      if (!jsonMatch) {
        throw new Error("No JSON array found in response");
      }
      const songs = JSON.parse(jsonMatch[0]);
      
      // Validate structure
      if (!Array.isArray(songs) || songs.length !== 10) {
        throw new Error(`Expected 10 songs, got ${songs.length}`);
      }
      
      // Validate each song has title and artist
      const validatedSongs = songs.map((song, idx) => {
        if (!song.title || !song.artist) {
          throw new Error(`Song ${idx + 1} missing title or artist`);
        }
        return {
          title: String(song.title).trim(),
          artist: String(song.artist).trim()
        };
      });
      
      return validatedSongs;
    } catch (parseErr) {
      logger.error("Failed to parse Groq DJ response", { error: parseErr.message, content });
      throw new AppError("Invalid song list format from AI", 400);
    }
  } catch (err) {
    if (err?.name === "AbortError" && timedOutStage.value) {
      throw new AppError(`Groq request timed out during ${timedOutStage.value}`, 504);
    }
    if (err instanceof AppError) throw err;
    if (isConnectionError(err)) {
      throw toServiceUnreachableError("Groq", err);
    }
    throw err;
  } finally {
    clearTimeout(connectTimeoutId);
    clearTimeout(writeTimeoutId);
  }
}

module.exports = { requestStructuredCompletion, generateSongRecommendations };
