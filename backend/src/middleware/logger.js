const appLogger = require("../utils/logger");
const crypto = require("crypto");

function logger(req, res, next) {
  // Generate or use existing request ID
  const requestId = req.headers["x-request-id"] || crypto.randomUUID();
  req.requestId = requestId;
  
  appLogger.info("HTTP request", {
    method: req.method,
    path: req.path,
    requestId: requestId,
  });
  next();
}

module.exports = { logger };
