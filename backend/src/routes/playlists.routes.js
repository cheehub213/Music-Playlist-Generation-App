const express = require("express");
const { listPlaylists } = require("../controllers/playlistController");
const { authMiddleware } = require("../middleware/auth.middleware");

const router = express.Router();
router.get("/", authMiddleware, listPlaylists);

module.exports = router;
