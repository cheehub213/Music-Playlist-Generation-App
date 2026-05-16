function canonicalGenre(genre) {
  const value = String(genre || "").toLowerCase().trim();
  const aliases = {
    lofi: "lo-fi",
    "lo fi": "lo-fi",
    edm: "dance",
    workout: "pop",
    studying: "ambient",
    nostalgic: "indie",
    energetic: "dance",
    calm: "ambient",
    focused: "lo-fi",
  };
  return aliases[value] || value;
}

function energyTarget(energy) {
  if (energy === "high") return 0.85;
  if (energy === "medium") return 0.6;
  return 0.35;
}

function mapMoodsToGenres(analysis) {
  const genres = (analysis.genres || []).map(canonicalGenre).filter(Boolean);
  const moodGenre = canonicalGenre(analysis.mood);
  return [...new Set([...genres, moodGenre].filter(Boolean))].slice(0, 5);
}

function generateSearchQueries(analysis) {
  const genres = mapMoodsToGenres(analysis);
  const keywordText = (analysis.keywords || []).slice(0, 3).join(" ");
  const vibe = analysis.vibe || analysis.mood;

  return [
    ...genres.map((genre) => `genre:"${genre}" ${vibe}`),
    `${analysis.mood} ${keywordText}`.trim(),
    `${vibe} ${analysis.energy} energy`.trim(),
  ].filter(Boolean);
}

function relevanceScore(track, analysis, audioFeature) {
  const haystack = `${track.title} ${track.artist} ${track.album}`.toLowerCase();
  const keywordScore = (analysis.keywords || []).reduce((score, keyword) => {
    return score + (haystack.includes(String(keyword).toLowerCase()) ? 0.08 : 0);
  }, 0);
  const popularityScore = Math.min((track.popularity || 0) / 100, 1) * 0.25;
  const target = energyTarget(analysis.energy);
  const featureEnergy = typeof audioFeature?.energy === "number" ? audioFeature.energy : target;
  const energyScore = (1 - Math.min(Math.abs(featureEnergy - target), 1)) * 0.45;
  return Number((0.3 + keywordScore + popularityScore + energyScore).toFixed(4));
}

module.exports = {
  mapMoodsToGenres,
  generateSearchQueries,
  relevanceScore,
};
