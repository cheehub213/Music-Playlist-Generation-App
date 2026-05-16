async function login({ email, password }) {
  if (!email || !password) {
    const err = new Error("Missing credentials");
    err.status = 400;
    throw err;
  }
  return "demo-token";
}

module.exports = { login };
