const express = require("express");
const { listTracks } = require("../controllers/trackController");

const router = express.Router();
router.get("/", listTracks);

module.exports = router;
