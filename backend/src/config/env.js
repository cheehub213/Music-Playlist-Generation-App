const dotenv = require("dotenv");
const path = require("path");

function loadEnv() {
  const envFile = process.env.ENV_FILE || ".env.local";
  const envPath = path.isAbsolute(envFile)
    ? envFile
    : path.resolve(__dirname, "..", "..", envFile);

  dotenv.config({ path: envPath });
}

module.exports = { loadEnv };
