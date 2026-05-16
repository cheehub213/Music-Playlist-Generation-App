const spotifyService = require("./spotifyService");
const recommendationEngine = require("./recommendationEngine");
const { AppError } = require("../utils/errors");
const logger = require("../utils/logger");

function energyScore(energy) {
  if (energy === "high") {
    return 0.9;
  }
  if (energy === "medium") {
    return 0.6;
  }
  return 0.35;
}

async function searchTracksForQuery(query) {
  const [tracks, artistsResult, albumsResult] = await Promise.all([
    spotifyService.searchTracksPaginated(query, 12),
    spotifyService.search(query, { type: "artist", limit: 4 }),
    spotifyService.search(query, { type: "album", limit: 4 }),
  ]);

  const expansionQueries = [
    ...artistsResult.artists.map((artist) => `artist:"${artist.name}"`),
    ...albumsResult.albums.map((album) => `album:"${album.title}" ${album.artist}`),
  ].filter(Boolean).slice(0, 4);

  if (expansionQueries.length === 0) {
    return tracks;
  }

  const expandedTracks = await Promise.all(
    expansionQueries.map((expandedQuery) => spotifyService.searchTracksPaginated(expandedQuery, 6))
  );

  return [...tracks, ...expandedTracks.flat()];
}

async function generatePlaylistFromAnalysis(analysis) {
  const genres = Array.isArray(analysis.genres) && analysis.genres.length > 0 ? analysis.genres : ["chill"];
  const keywords = Array.isArray(analysis.keywords) ? analysis.keywords : [];
  const queries = recommendationEngine.generateSearchQueries(analysis);
  const tracksById = new Map();

  const searchResults = await Promise.all(
    queries.slice(0, 4).map((query) => searchTracksForQuery(query))
  );

  for (const tracks of searchResults) {
    tracks.forEach((track) => {
      if (track.id && !tracksById.has(track.id)) {
        tracksById.set(track.id, track);
      }
    });
    if (tracksById.size >= 30) break;
  }

  const rawTracks = [...tracksById.values()];
  if (rawTracks.length === 0) {
    throw new AppError("Spotify did not return matching tracks for this mood", 404);
  }

  let features = [];
  try {
    features = await spotifyService.getAudioFeatures(rawTracks.map((track) => track.id));
  } catch (err) {
    logger.warn("Spotify audio features unavailable; continuing with search relevance", {
      error: err.message,
    });
  }

  const featureById = new Map(features.map((feature) => [feature.id, feature]));
  const tracks = rawTracks
    .map((track) => ({
      ...track,
      relevanceScore: recommendationEngine.relevanceScore(track, analysis, featureById.get(track.id)),
      audioFeatures: featureById.get(track.id) || null,
    }))
    .sort((a, b) => b.relevanceScore - a.relevanceScore)
    .slice(0, 20);

  if (tracks.length < 20) {
    logger.warn("Generated playlist has fewer than 20 tracks", {
      count: tracks.length,
      queries,
    });
  }

  return {
    mood: analysis.mood,
    emotions: analysis.emotions,
    genres,
    energy: analysis.energy,
    vibe: analysis.vibe,
    keywords,
    playlistTitle: analysis.playlistTitle,
    playlist: {
      id: `generated-${Date.now()}`,
      title: analysis.playlistTitle,
      description: `A ${analysis.vibe} mix tuned for a ${analysis.energy} energy level.`,
      genres,
      coverImage: tracks[0]?.artworkUrl || "",
      moodTags: [...new Set([analysis.mood, analysis.vibe, ...genres].filter(Boolean))].slice(0, 6),
      energyScore: energyScore(analysis.energy),
      tracks,
      spotifySyncStatus: "generated",
    },
  };
}

module.exports = { generatePlaylistFromAnalysis };
