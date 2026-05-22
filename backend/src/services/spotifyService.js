const { getSpotifyConfig } = require("../config/spotify");
const { AppError } = require("../utils/errors");
const logger = require("../utils/logger");

let cachedToken = null;
let tokenExpiresAt = 0;

function encodeCredentials(clientId, clientSecret) {
  return Buffer.from(`${clientId}:${clientSecret}`, "utf8").toString("base64");
}

function errorMessageFromBody(data, fallback) {
  return data?.error_description
    || data?.error?.message
    || data?.error
    || data?.raw
    || fallback;
}

function getNetworkErrorCode(err) {
  return err?.code || err?.cause?.code || "";
}

function isConnectionError(err) {
  const code = getNetworkErrorCode(err);
  return code === "ENOTFOUND"
    || code === "ECONNREFUSED"
    || code === "EAI_AGAIN"
    || /connect/i.test(String(err?.message || ""));
}

function toServiceUnreachableError(serviceName, err) {
  const code = getNetworkErrorCode(err);
  const codeSuffix = code ? ` (${code})` : "";
  return new AppError(`${serviceName} service unreachable${codeSuffix}. Check network or base URL.`, 503);
}

async function fetchJsonWithTimeouts(url, options, timeouts, serviceName) {
  const controller = new AbortController();
  const timedOutStage = { value: null };

  const connectTimeoutId = setTimeout(() => {
    timedOutStage.value = "connect";
    controller.abort();
  }, timeouts.connectTimeoutMs);

  const writeTimeoutId = setTimeout(() => {
    timedOutStage.value = "write";
    controller.abort();
  }, timeouts.writeTimeoutMs);

  try {
    const response = await fetch(url, {
      ...options,
      signal: controller.signal,
    });

    clearTimeout(connectTimeoutId);
    clearTimeout(writeTimeoutId);

    const readTimeoutId = setTimeout(() => {
      timedOutStage.value = "read";
      controller.abort();
    }, timeouts.readTimeoutMs);

    let text = "";
    try {
      text = await response.text();
    } finally {
      clearTimeout(readTimeoutId);
    }

    let data;
    try {
      data = JSON.parse(text);
    } catch (err) {
      data = { raw: text };
    }

    return { response, data };
  } catch (err) {
    if (err?.name === "AbortError" && timedOutStage.value) {
      throw new AppError(`${serviceName} request timed out during ${timedOutStage.value}`, 504);
    }
    throw err;
  } finally {
    clearTimeout(connectTimeoutId);
    clearTimeout(writeTimeoutId);
  }
}

function normalizeTrack(track) {
  return {
    id: track.id,
    title: track.name,
    artist: Array.isArray(track.artists)
      ? track.artists.map((artist) => artist.name).filter(Boolean).join(", ")
      : "",
    album: track.album?.name || "",
    durationMs: track.duration_ms,
    uri: track.uri,
    previewUrl: track.preview_url,
    externalUrl: track.external_urls?.spotify || "",
    artworkUrl: track.album?.images?.[0]?.url || "",
    popularity: track.popularity || 0,
    source: "spotify",
  };
}

function normalizeArtist(artist) {
  return {
    id: artist.id,
    name: artist.name,
    genres: artist.genres || [],
    followers: artist.followers?.total || 0,
    popularity: artist.popularity || 0,
    imageUrl: artist.images?.[0]?.url || "",
  };
}

function normalizeAlbum(album) {
  return {
    id: album.id,
    title: album.name,
    artist: album.artists?.map((artist) => artist.name).filter(Boolean).join(", ") || "",
    releaseDate: album.release_date || "",
    totalTracks: album.total_tracks || 0,
    artworkUrl: album.images?.[0]?.url || "",
  };
}

function normalizePlaylist(playlist) {
  return {
    id: playlist.id,
    title: playlist.name,
    description: playlist.description || "",
    artworkUrl: playlist.images?.[0]?.url || "",
    followers: playlist.followers?.total || 0,
    creator: playlist.owner?.display_name || "",
    totalTracks: playlist.tracks?.total || 0,
    tracks: [],
  };
}

async function requestAccessToken(config = getSpotifyConfig()) {
  if (!config.clientId || !config.clientSecret) {
    throw new AppError("Missing SPOTIFY_CLIENT_ID or SPOTIFY_CLIENT_SECRET in environment", 500);
  }

  const now = Date.now();
  if (cachedToken && now < tokenExpiresAt) {
    return cachedToken;
  }

  try {
    const { response, data } = await fetchJsonWithTimeouts(config.tokenUrl, {
      method: "POST",
      headers: {
        Authorization: `Basic ${encodeCredentials(config.clientId, config.clientSecret)}`,
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({ grant_type: "client_credentials" }).toString(),
    }, config, "Spotify token");
    if (!response.ok) {
      const message = errorMessageFromBody(data, "Spotify token request failed");
      logger.warn("Spotify token request failed", { status: response.status, message });
      throw new AppError(message, response.status);
    }

    cachedToken = data.access_token;
    tokenExpiresAt = now + Math.max(0, Number(data.expires_in || 0) - 60) * 1000;
    return cachedToken;
  } catch (err) {
    if (err instanceof AppError) {
      throw err;
    }
    if (isConnectionError(err)) {
      throw toServiceUnreachableError("Spotify", err);
    }
    throw err;
  }
}

async function requestSpotifyJson(pathOrUrl, { searchParams, method = "GET" } = {}) {
  const config = getSpotifyConfig();
  const token = await requestAccessToken(config);
  const url = pathOrUrl.startsWith("http")
    ? new URL(pathOrUrl)
    : new URL(`${config.baseUrl}${pathOrUrl.startsWith("/") ? "" : "/"}${pathOrUrl}`);

  Object.entries(searchParams || {}).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      url.searchParams.set(key, String(value));
    }
  });

  try {
    const { response, data } = await fetchJsonWithTimeouts(url, {
      method,
      headers: { Authorization: `Bearer ${token}` },
    }, config, "Spotify");
    if (!response.ok) {
      const message = errorMessageFromBody(data, "Spotify request failed");
      logger.warn("Spotify request failed", { status: response.status, message, path: url.pathname });
      throw new AppError(message, response.status);
    }
    return data;
  } catch (err) {
    if (err instanceof AppError) {
      throw err;
    }
    if (isConnectionError(err)) {
      throw toServiceUnreachableError("Spotify", err);
    }
    throw err;
  }
}

async function search(query, { limit = 10, offset = 0, type = "track,artist,album,playlist" } = {}) {
  const normalizedQuery = String(query || "").trim();
  if (!normalizedQuery) {
    return { query: normalizedQuery, tracks: [], artists: [], albums: [], playlists: [] };
  }

  const actualLimit = Math.min(Math.max(Number(limit) || 10, 1), 10);
  const actualOffset = Math.max(Number(offset) || 0, 0);

  const data = await requestSpotifyJson(getSpotifyConfig().searchUrl, {
    searchParams: {
      q: normalizedQuery,
      type,
      limit: actualLimit,
      offset: actualOffset,
    },
  });

  return {
    query: normalizedQuery,
    tracks: Array.isArray(data.tracks?.items) ? data.tracks.items.filter(Boolean).map(normalizeTrack) : [],
    artists: Array.isArray(data.artists?.items) ? data.artists.items.filter(Boolean).map(normalizeArtist) : [],
    albums: Array.isArray(data.albums?.items) ? data.albums.items.filter(Boolean).map(normalizeAlbum) : [],
    playlists: Array.isArray(data.playlists?.items) ? data.playlists.items.filter(Boolean).map(normalizePlaylist) : [],
    total: data.tracks?.total || data.artists?.total || data.albums?.total || data.playlists?.total || 0,
    nextOffset: data.tracks?.next || data.artists?.next || data.albums?.next || data.playlists?.next
      ? actualOffset + actualLimit
      : null,
  };
}

async function searchTracksPaginated(query, minimum = 20) {
  const tracks = [];
  const seen = new Set();
  let offset = 0;

  while (tracks.length < minimum && offset < 150) {
    const page = await search(query, { limit: 10, offset });
    page.tracks.forEach((track) => {
      if (track.id && !seen.has(track.id)) {
        seen.add(track.id);
        tracks.push(track);
      }
    });
    if (page.nextOffset === null || page.tracks.length === 0) {
      break;
    }
    offset = page.nextOffset;
  }

  return tracks;
}

async function fetchCategoryPlaylists(config, categoryId) {
  const data = await requestSpotifyJson(`/browse/categories/${categoryId}/playlists`, {
    searchParams: { limit: 10, country: config.country },
  });
  return Array.isArray(data.playlists?.items) ? data.playlists.items.filter(Boolean).map(normalizePlaylist) : [];
}

async function featuredPlaylists() {
  const config = getSpotifyConfig();
  try {
    const data = await requestSpotifyJson("/browse/featured-playlists", {
      searchParams: { limit: 10, country: config.country },
    });
    return Array.isArray(data.playlists?.items) ? data.playlists.items.filter(Boolean).map(normalizePlaylist) : [];
  } catch (err) {
    if (!(err instanceof AppError) || (err.status !== 403 && err.status !== 404)) {
      throw err;
    }

    logger.warn("Spotify featured playlists unavailable; falling back to category playlists", {
      status: err.status,
      country: config.country,
    });

    try {
      const categories = await requestSpotifyJson("/browse/categories", {
        searchParams: { limit: 10, country: config.country },
      });
      const categoryIds = Array.isArray(categories.categories?.items)
        ? categories.categories.items.map((category) => category.id).filter(Boolean)
        : [];
      const preferredCategoryIds = ["toplists", "pop", "mood", "party", "workout"];
      const orderedCategoryIds = [
        ...preferredCategoryIds.filter((categoryId) => categoryIds.includes(categoryId)),
        ...categoryIds.filter((categoryId) => !preferredCategoryIds.includes(categoryId)),
      ].slice(0, 5);

      for (const categoryId of orderedCategoryIds) {
        const playlists = await fetchCategoryPlaylists(config, categoryId);
        if (playlists.length > 0) {
          return playlists;
        }
      }
    } catch (fallbackErr) {
      if (!(fallbackErr instanceof AppError) || (fallbackErr.status !== 403 && fallbackErr.status !== 404)) {
        throw fallbackErr;
      }
      logger.warn("Spotify category playlist fallback unavailable", {
        status: fallbackErr.status,
        country: config.country,
      });
    }

    return [];
  }
}

async function artistDetails(artistId) {
  const [artist, albums, topTracks, relatedArtists] = await Promise.all([
    requestSpotifyJson(`/artists/${artistId}`),
    requestSpotifyJson(`/artists/${artistId}/albums`, { searchParams: { limit: 10, include_groups: "album,single" } }),
    requestSpotifyJson(`/artists/${artistId}/top-tracks`, { searchParams: { market: "US" } }),
    requestSpotifyJson(`/artists/${artistId}/related-artists`),
  ]);
  return {
    artist: normalizeArtist(artist),
    albums: Array.isArray(albums.items) ? albums.items.map(normalizeAlbum) : [],
    topTracks: Array.isArray(topTracks.tracks) ? topTracks.tracks.map(normalizeTrack) : [],
    relatedArtists: Array.isArray(relatedArtists.artists) ? relatedArtists.artists.map(normalizeArtist) : [],
  };
}

async function playlistDetails(playlistId) {
  const playlist = await requestSpotifyJson(`/playlists/${playlistId}`, {
    searchParams: { market: "US" },
  });
  const tracks = Array.isArray(playlist.tracks?.items)
    ? playlist.tracks.items.map((item) => item.track).filter(Boolean).map(normalizeTrack)
    : [];
  return {
    ...normalizePlaylist(playlist),
    tracks,
    durationMs: tracks.reduce((total, track) => total + (track.durationMs || 0), 0),
  };
}

async function getAudioFeatures(trackIds) {
  const ids = [...new Set((trackIds || []).filter(Boolean))].slice(0, 100);
  if (ids.length === 0) {
    return [];
  }
  const data = await requestSpotifyJson("/audio-features", {
    searchParams: { ids: ids.join(",") },
  });
  return Array.isArray(data.audio_features) ? data.audio_features.filter(Boolean) : [];
}

module.exports = { search, searchTracksPaginated, getAudioFeatures, requestAccessToken, featuredPlaylists, artistDetails, playlistDetails };
