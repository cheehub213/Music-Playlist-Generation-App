const { pool } = require("../config/database");

async function listPlaylists(userId) {
  const result = await pool.query('SELECT * FROM playlists WHERE user_id = $1', [userId]);
  return result.rows;
}

module.exports = { listPlaylists };
