const trackService = require("../services/trackService");

async function listTracks(req, res, next) {
  try {
    const data = await trackService.listTracks();
    res.json(data);
  } catch (err) {
    next(err);
  }
}

module.exports = { listTracks };
