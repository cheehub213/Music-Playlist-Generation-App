function requireFields(body, fields) {
  fields.forEach((field) => {
    if (!body[field]) {
      throw new Error(`Missing field: ${field}`);
    }
  });
}

module.exports = { requireFields };
