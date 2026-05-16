require("../src/config/env").loadEnv();

const { getGroqConfig } = require("../src/config/groq");

(async () => {
  try {
    const config = getGroqConfig();
    const res = await fetch(config.modelsUrl, {
      headers: { Authorization: `Bearer ${config.apiKey}` },
    });
    const data = await res.json();
    console.log(JSON.stringify(data, null, 2));
  } catch (e) {
    console.error("FETCH_ERR", e.message);
  }
})();
