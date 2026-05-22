require('../src/config/env').loadEnv(); const s = require('../src/services/spotifyService'); s.searchTracksPaginated('happy', 12).then(() => console.log('OK1')).catch(console.error);
