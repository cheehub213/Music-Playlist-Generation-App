const express = require("express");
const { getProfile } = require("../controllers/userController");
const { authMiddleware } = require("../middleware/auth.middleware");

const router = express.Router();
router.get("/me", authMiddleware, getProfile);

module.exports = router;
