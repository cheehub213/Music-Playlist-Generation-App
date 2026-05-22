require('dotenv').config();
const { loadEnv } = require("./config/env");
// LOAD ENV VARS BEFORE ANY OTHER IMPORTS!
loadEnv();

const express = require("express");
const cors = require("cors");
const { getSpotifyConfig } = require("./config/spotify");
const { errorHandler } = require("./middleware/errorHandler");
const { logger: requestLogger } = require("./middleware/logger");
const appLogger = require("./utils/logger");

const authRoutes = require("./routes/auth.routes");
const usersRoutes = require("./routes/users.routes");
const playlistsRoutes = require("./routes/playlists.routes");
const tracksRoutes = require("./routes/tracks.routes");
const recommendationsRoutes = require("./routes/recommendations.routes");
const spotifyRoutes = require("./routes/spotify.routes");
const searchRoutes = require("./routes/search.routes");
const notificationsRoutes = require("./routes/notifications.routes");
const devRoutes = require("./routes/dev.routes");

const spotifyConfig = getSpotifyConfig();
appLogger.info("Spotify env loaded", {
	clientId: spotifyConfig.clientId || null,
});

const app = express();
app.use(cors());
app.use(express.json());
app.use(requestLogger);

app.use("/auth", authRoutes);
app.use("/users", usersRoutes);
app.use("/playlists", playlistsRoutes);
app.use("/tracks", tracksRoutes);
app.use("/recommendations", recommendationsRoutes);
app.use("/spotify", spotifyRoutes);
app.use("/search", searchRoutes);
app.use("/notifications", notificationsRoutes);

// Dev-only AI test routes. Enable locally by setting ALLOW_DEV_AI_TEST=true in .env.local
if (process.env.ALLOW_DEV_AI_TEST === "true") {
	app.use("/dev", devRoutes);
}

app.get("/health", (req, res) => res.json({ status: "ok" }));

app.use(errorHandler);

module.exports = app;
