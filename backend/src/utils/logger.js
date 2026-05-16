function safeMeta(meta) {
  if (!meta || typeof meta !== "object") {
    return {};
  }

  const clone = { ...meta };
  if (clone.apiKey) {
    clone.apiKey = "[REDACTED]";
  }
  if (clone.authorization) {
    clone.authorization = "[REDACTED]";
  }
  if (clone.prompt && typeof clone.prompt === "string") {
    clone.promptPreview = clone.prompt.slice(0, 80);
    delete clone.prompt;
  }

  return clone;
}

function log(level, message, meta = {}) {
  const payload = {
    level,
    message,
    timestamp: new Date().toISOString(),
    ...safeMeta(meta),
  };
  console.log(JSON.stringify(payload));
}

function info(message, meta) {
  log("info", message, meta);
}

function warn(message, meta) {
  log("warn", message, meta);
}

function error(message, meta) {
  log("error", message, meta);
}

module.exports = { info, warn, error };
