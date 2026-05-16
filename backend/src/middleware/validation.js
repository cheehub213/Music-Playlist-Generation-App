function validate(schema) {
  return (req, res, next) => {
    try {
      schema(req);
      next();
    } catch (err) {
      res.status(400).json({ error: err.message });
    }
  };
}

module.exports = { validate };
