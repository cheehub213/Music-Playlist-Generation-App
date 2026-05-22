const recommendationService = require("../services/recommendationService");

async function getRecommendations(req, res, next) {
  try {
    const data = await recommendationService.getRecommendations(req.user?.id);
    res.json(data);
  } catch (err) {
    next(err);
  }
}

async function generateFromMood(req, res, next) {
  try {
    const moodPrompt = req.body?.prompt;
    const data = await recommendationService.generateRecommendationsFromMood({
      userId: req.user?.id,
      moodPrompt,
    });
    res.json(data);
  } catch (err) {
    next(err);
  }
}

async function generateFromGroqDJ(req, res, next) {
  try {
    const moodPrompt = req.body?.prompt;
    const data = await recommendationService.generatePlaylistFromGroqDJ({
      userId: req.user?.id,
      moodPrompt,
    });
    res.json(data);
  } catch (err) {
    next(err);
  }
}

async function analyzeMood(req, res, next) {
  try {
    const moodPrompt = req.body?.prompt;
    const data = await recommendationService.analyzeMoodPrompt(moodPrompt);
    res.json(data);
  } catch (err) {
    next(err);
  }
}

module.exports = { getRecommendations, generateFromMood, analyzeMood, generateFromGroqDJ };
