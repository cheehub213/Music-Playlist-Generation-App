const authService = require("../services/authService");

async function login(req, res, next) {
  try {
    const { token, user } = await authService.login(req.body);
    res.json({ token, user });
  } catch (err) {
    next(err);
  }
}

async function register(req, res, next) {
  try {
    const { token, user } = await authService.register(req.body);
    res.status(201).json({ token, user });
  } catch (err) {
    next(err);
  }
}

module.exports = { login, register };
