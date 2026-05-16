function rateLimiter(req, res, next) {
  return next();
}

module.exports = { rateLimiter };
