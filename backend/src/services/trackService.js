const { pool } = require("../config/database");

async function listTracks() {
  const result = await pool.query('SELECT * FROM tracks');
  return result.rows;
}

module.exports = { listTracks };
