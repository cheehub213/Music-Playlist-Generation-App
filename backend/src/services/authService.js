const { pool } = require("../config/database");
const { hash } = require("../utils/encryption");
const { signToken } = require("../utils/jwt");

async function login({ email, password }) {
  if (!email || !password) {
    const err = new Error("Missing credentials");
    err.status = 400;
    throw err;
  }
  
  const result = await pool.query('SELECT * FROM users WHERE email = $1', [email]);
  const user = result.rows[0];

  if (!user || user.password_hash !== hash(password)) {
    const err = new Error("Invalid email or password");
    err.status = 401;
    throw err;
  }

  const token = signToken({ userId: user.id });
  return { token, user: { id: user.id, email: user.email, name: user.name } };
}

async function register({ email, password, name }) {
  if (!email || !password || !name) {
    const err = new Error("Missing credentials");
    err.status = 400;
    throw err;
  }

  const existing = await pool.query('SELECT id FROM users WHERE email = $1', [email]);
  if (existing.rowCount > 0) {
    const err = new Error("Email already registered");
    err.status = 409;
    throw err;
  }

  const result = await pool.query(
    'INSERT INTO users (email, password_hash, name) VALUES ($1, $2, $3) RETURNING id, email, name',
    [email, hash(password), name]
  );
  
  const user = result.rows[0];
  const token = signToken({ userId: user.id });
  return { token, user };
}

module.exports = { login, register };
