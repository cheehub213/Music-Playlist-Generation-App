require("../src/config/env").loadEnv();

const { getSpotifyConfig } = require("../src/config/spotify");
const spotifyService = require("../src/services/spotifyService");

function redact(value) {
  if (!value) {
    return "";
  }
  if (value.length <= 8) {
    return "***";
  }
  return `${value.slice(0, 4)}***${value.slice(-4)}`;
}

async function main() {
  const config = getSpotifyConfig();
  console.log("Spotify config:", {
    hasClientId: Boolean(config.clientId),
    clientId: redact(config.clientId),
    hasClientSecret: Boolean(config.clientSecret),
    tokenUrl: config.tokenUrl,
    baseUrl: config.baseUrl,
    searchUrl: config.searchUrl,
    timeoutMs: config.timeoutMs,
  });

  await spotifyService.requestAccessToken(config);
  console.log("Client credentials auth check: ok");

  try {
    const result = await spotifyService.search("Daft Punk");
    console.log("Track search check: ok", {
      query: result.query,
      resultCount: result.results.length,
      firstTrack: result.results[0]
        ? {
            title: result.results[0].title,
            artist: result.results[0].artist,
            source: result.results[0].source,
          }
        : null,
    });
  } catch (err) {
    if (err.status === 403 && /premium subscription/i.test(err.message)) {
      console.log("Track search check: blocked by Spotify account policy", {
        status: err.status,
        message: err.message,
      });
      return;
    }
    throw err;
  }
}

main().catch((err) => {
  console.error("Spotify check failed:", err.message);
  process.exitCode = 1;
});
