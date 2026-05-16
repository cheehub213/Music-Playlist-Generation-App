async function getProfile(userId) {
  return { id: userId, name: "Guest" };
}

module.exports = { getProfile };
