const express = require('express');
const { testGroq } = require('../controllers/devController');

const router = express.Router();

router.get('/test-groq', testGroq);
router.post('/test-groq', testGroq);

module.exports = router;
