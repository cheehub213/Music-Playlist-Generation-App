const express = require("express");
const {
	getRecommendations,
	generateFromMood,
	analyzeMood,
} = require("../controllers/recommendationController");
const { authMiddleware } = require("../middleware/auth.middleware");

const router = express.Router();
router.get("/", authMiddleware, getRecommendations);
router.post("/analyze", analyzeMood);
router.post("/generate", generateFromMood);

module.exports = router;
