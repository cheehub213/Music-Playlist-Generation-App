const { pool } = require("../config/database");

async function getProfile(userId) {
  const result = await pool.query('SELECT id, email, name, created_at FROM users WHERE id = $1', [userId]);
  if (result.rowCount === 0) {
    const err = new Error("User not found");
    err.status = 404;
    throw err;
  }
  return result.rows[0];
}

module.exports = { getProfile };
