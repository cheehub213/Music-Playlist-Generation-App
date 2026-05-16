const moodAnalysisService = require('../services/moodAnalysisService');
const logger = require('../utils/logger');

async function testGroq(req, res, next) {
  try {
    const prompt = req.query.prompt || req.body?.prompt || 'I want something nostalgic and calm';
    const analysis = await moodAnalysisService.analyzeMood(prompt);
    res.json({ ok: true, analysis });
  } catch (err) {
    logger.warn('Dev Groq test failed', { error: err.message });
    res.status(500).json({ ok: false, error: err.message });
  }
}

module.exports = { testGroq };
