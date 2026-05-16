async function listPlaylists(userId) {
  return [{ id: "1", name: "Favorites", userId }];
}

module.exports = { listPlaylists };
