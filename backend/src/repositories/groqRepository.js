const { requestStructuredCompletion } = require("../services/groqService");

async function analyzeMood(prompt) {
  return requestStructuredCompletion({
    systemPrompt: [
      "You are AuraBeat's mood intelligence layer.",
      "Extract mood information for music recommendation.",
      "Output only valid JSON.",
    ].join(" "),
    userPrompt: `Analyze this mood prompt for music generation: "${prompt}"`,
    schemaHint: [
      '{"mood":"string","emotions":["string"],"genres":["string"],"energy":"low|medium|high","tempo":"slow|moderate|fast","vibe":"string","keywords":["string"],"playlistTitle":"string"}',
    ].join("\n"),
  });
}

module.exports = { analyzeMood };
