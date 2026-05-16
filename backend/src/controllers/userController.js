const userService = require("../services/userService");

async function getProfile(req, res, next) {
  try {
    const profile = await userService.getProfile(req.user?.id);
    res.json(profile);
  } catch (err) {
    next(err);
  }
}

module.exports = { getProfile };
