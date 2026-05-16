const playlistService = require("../services/playlistService");

async function listPlaylists(req, res, next) {
  try {
    const data = await playlistService.listPlaylists(req.user?.id);
    res.json(data);
  } catch (err) {
    next(err);
  }
}

module.exports = { listPlaylists };
