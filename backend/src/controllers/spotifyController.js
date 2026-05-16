const spotifyService = require("../services/spotifyService");

async function search(req, res, next) {
  try {
    const data = await spotifyService.search(req.query.q || "", {
      limit: req.query.limit,
      offset: req.query.offset,
      type: req.query.type,
    });
    res.json(data);
  } catch (err) {
    next(err);
  }
}

async function featuredPlaylists(req, res, next) {
  try {
    res.json(await spotifyService.featuredPlaylists());
  } catch (err) {
    next(err);
  }
}

async function artistDetails(req, res, next) {
  try {
    res.json(await spotifyService.artistDetails(req.params.id));
  } catch (err) {
    next(err);
  }
}

async function playlistDetails(req, res, next) {
  try {
    res.json(await spotifyService.playlistDetails(req.params.id));
  } catch (err) {
    next(err);
  }
}

module.exports = { search, featuredPlaylists, artistDetails, playlistDetails };
