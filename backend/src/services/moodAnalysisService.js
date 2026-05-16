const groqRepository = require("../repositories/groqRepository");
const logger = require("../utils/logger");
const { AppError } = require("../utils/errors");

function normalizeAnalysis(raw, prompt) {
  if (!raw || typeof raw !== "object") {
    throw new AppError("Grok returned an empty mood analysis", 502);
  }

  const genres = Array.isArray(raw.genres) ? raw.genres.map(String).filter(Boolean).slice(0, 6) : [];
  const keywords = Array.isArray(raw.keywords) ? raw.keywords.map(String).filter(Boolean).slice(0, 10) : [];
  const emotions = Array.isArray(raw.emotions) ? raw.emotions.map(String).filter(Boolean).slice(0, 6) : [];
  const energy = String(raw.energy || "").toLowerCase();

  if (!raw.mood || genres.length === 0 || !["low", "medium", "high"].includes(energy) || !raw.vibe) {
    throw new AppError("Grok returned an incomplete mood analysis", 502);
  }

  return {
    mood: String(raw.mood).toLowerCase(),
    emotions: emotions.length > 0 ? emotions : [String(raw.mood).toLowerCase()],
    genres,
    energy,
    tempo: ["slow", "moderate", "fast"].includes(String(raw.tempo).toLowerCase())
      ? String(raw.tempo).toLowerCase()
      : energy === "high"
      ? "fast"
      : energy === "medium"
      ? "moderate"
      : "slow",
    vibe: String(raw.vibe),
    keywords,
    playlistTitle: String(raw.playlistTitle || `${raw.mood} Mix`),
    source: "grok",
  };
}

async function analyzeMood(prompt) {
  try {
    const raw = await groqRepository.analyzeMood(prompt);
    const analysis = normalizeAnalysis(raw, prompt);
    logger.info("Grok mood analysis completed", {
      mood: analysis.mood,
      genres: analysis.genres,
      energy: analysis.energy,
    });
    return analysis;
  } catch (err) {
    logger.warn("Grok mood analysis failed", {
      error: err.message,
      prompt,
    });
    throw err;
  }
}

module.exports = { analyzeMood };
