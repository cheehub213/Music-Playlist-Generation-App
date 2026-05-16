const moodAnalysisService = require("./moodAnalysisService");
const { generatePlaylistFromAnalysis } = require("./playlistGenerationService");

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
};
