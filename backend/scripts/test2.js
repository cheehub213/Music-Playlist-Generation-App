require('../src/config/env').loadEnv(); require('../src/services/spotifyService').search('genre:pop happy', {type: 'artist', limit: 4}).then(() => console.log('OK')).catch(console.error);
