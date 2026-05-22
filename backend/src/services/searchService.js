const { searchTracks } = require("./spotifyService");

async function search(query) {
  try {
    const hits = await searchTracks(query, { limit: 20 });
    return { query, hits };
  } catch (err) {
    return { query, hits: [], error: err.message };
  }
}

module.exports = { search };
