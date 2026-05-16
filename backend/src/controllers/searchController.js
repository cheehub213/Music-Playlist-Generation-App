const searchService = require("../services/searchService");

async function search(req, res, next) {
  try {
    const data = await searchService.search(req.query.q || "");
    res.json(data);
  } catch (err) {
    next(err);
  }
}

module.exports = { search };
