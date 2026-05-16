const appLogger = require("../utils/logger");

function logger(req, res, next) {
  appLogger.info("HTTP request", {
    method: req.method,
    path: req.path,
    requestId: req.headers["x-request-id"] || null,
  });
  next();
}

module.exports = { logger };
