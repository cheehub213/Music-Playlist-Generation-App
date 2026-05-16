const express = require("express");
const { search, featuredPlaylists, artistDetails, playlistDetails } = require("../controllers/spotifyController");

const router = express.Router();
router.get("/search", search);
router.get("/featured-playlists", featuredPlaylists);
router.get("/artists/:id", artistDetails);
router.get("/playlists/:id", playlistDetails);

module.exports = router;
