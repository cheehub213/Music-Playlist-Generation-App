const moodAnalysisService = require("./moodAnalysisService");
const groqService = require("./groqService");
const spotifyService = require("./spotifyService");
const { generatePlaylistFromAnalysis } = require("./playlistGenerationService");
const { AppError } = require("../utils/errors");
const logger = require("../utils/logger");

async function analyzeMoodPrompt(moodPrompt) {
  const prompt = String(moodPrompt || "Surprise me with a balanced vibe");
  return moodAnalysisService.analyzeMood(prompt);
}

async function generateRecommendationsFromMood({ userId, moodPrompt }) {
  const prompt = String(moodPrompt || "Surprise me with a balanced vibe");
  const analysis = await moodAnalysisService.analyzeMood(prompt);
  const recommendation = await generatePlaylistFromAnalysis(analysis);

  return {
    userId: userId || null,
    inputPrompt: prompt,
    generatedAt: new Date().toISOString(),
    ...recommendation,
  };
}

async function generatePlaylistFromGroqDJ({ userId, moodPrompt }) {
  // Require explicit prompt - no defaults for Groq DJ
  if (!moodPrompt || typeof moodPrompt !== 'string' || moodPrompt.trim().length === 0) {
    throw new AppError("Groq DJ requires a valid mood prompt", 400);
  }
  
  const prompt = moodPrompt.trim();
  
  // Step 1: Get 10 songs from Groq (no fallback)
  logger.info("Groq DJ: Generating song recommendations", { prompt });
  const songs = await groqService.generateSongRecommendations(prompt);
  
  logger.info("Groq DJ: Received recommendations", { count: songs.length });
  
  // Step 2: Search Spotify for each song and fetch metadata (Sequentially to avoid rate limits or fetch socket drops)
  const spotifyTracks = [];
  for (const song of songs) {
    const track = await searchAndEnrichTrack(song);
    spotifyTracks.push(track);
    // Optional: Add a tiny delay to be gentle to the Spotify API and Node's fetch sockets
    await new Promise(resolve => setTimeout(resolve, 100));
  }
  
  // Filter out failed lookups
  const successfulTracks = spotifyTracks.filter(t => t !== null);
  
  if (successfulTracks.length === 0) {
    throw new AppError("Could not find any of the AI-recommended songs on Spotify", 404);
  }

  logger.info("Groq DJ: Successfully enriched tracks", { total: songs.length, found: successfulTracks.length });

  return {
    userId: userId || null,
    inputPrompt: prompt,
    generatedAt: new Date().toISOString(),
    playlist: {
      id: `groq-dj-${Date.now()}`,
      title: `AI DJ Mix - ${prompt.slice(0, 30)}${prompt.length > 30 ? "..." : ""}`,
      description: `A curated mix for: ${prompt}`,
      tracks: successfulTracks,
      spotifySyncStatus: "generated",
      source: "groq-dj"
    }
  };
}

async function searchAndEnrichTrack(song) {
  try {
    const searchQuery = `${song.title} ${song.artist}`;
    const results = await spotifyService.search(searchQuery, { 
      limit: 5, 
      type: "track" 
    });
    
    if (results.tracks.length === 0) {
      logger.warn(`Track not found on Spotify: ${song.title} - ${song.artist}`);
      return null;
    }
    
    return results.tracks[0]; // Return first (most relevant) match
  } catch (err) {
    logger.error(`Failed to search track on Spotify: ${song.title} - ${song.artist}`, { error: err.message });
    return null;
  }
}

async function getRecommendations(userId) {
  // Backward-compatible default path for existing clients.
  return generateRecommendationsFromMood({
    userId,
    moodPrompt: "default balanced mood",
  });
}

module.exports = {
  getRecommendations,
  generateRecommendationsFromMood,
  analyzeMoodPrompt,
  generatePlaylistFromGroqDJ,
};
