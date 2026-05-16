const logger = require("../utils/logger");

function errorHandler(err, req, res, next) {
  const status = err.status || 500;
  logger.error("Request failed", {
    status,
    path: req.path,
    method: req.method,
    error: err.message,
  });

  const publicMessage = status === 504 || status === 502 || status === 503
    ? err.message
    : status >= 500
      ? "Server error"
      : err.message || "Request failed";
  res.status(status).json({ error: publicMessage });
}

module.exports = { errorHandler };
