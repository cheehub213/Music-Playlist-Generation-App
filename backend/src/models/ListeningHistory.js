class ListeningHistory {
  constructor({ userId, trackId, playedAt }) {
    this.userId = userId;
    this.trackId = trackId;
    this.playedAt = playedAt;
  }
}

module.exports = ListeningHistory;
