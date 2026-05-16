# AuraBeat Backend

Express API for the AuraBeat platform.

## Local run

From `AuraBeat/backend`:

```sh
npm install
npm run dev
```

Health check:

```text
http://127.0.0.1:3000/health
```

## Groq AI Integration

AuraBeat uses Groq through backend-only service calls.

### Environment variables

Copy `.env.example` to `.env.local` and set:

- `GROQ_API_KEY`
- `GROQ_BASE_URL` (default: `https://api.groq.com/openai/v1`)
- `GROQ_API_URL` (default: `https://api.groq.com/openai/v1/chat/completions`)
- `GROQ_MODEL` (default: `llama-3.3-70b-versatile`)
- `GROQ_TIMEOUT_MS`
- `GROQ_CONNECT_TIMEOUT_MS` (default: `2000`)
- `GROQ_READ_TIMEOUT_MS` (default: `120000`)
- `GROQ_WRITE_TIMEOUT_MS` (default: `30000`)
- `GROQ_MAX_RETRIES`

### Security model

- Android never calls Groq directly.
- Only backend routes call Groq.
- API keys are loaded from environment variables.
- Logs redact secrets and avoid dumping full prompts.

### Local verification

Run a redacted live API check from `AuraBeat/backend`:

```sh
npm run test:groq
```

## Spotify Integration

Spotify credentials are backend-only and loaded from environment variables:

- `SPOTIFY_CLIENT_ID`
- `SPOTIFY_CLIENT_SECRET`
- `SPOTIFY_TOKEN_URL` (default: `https://accounts.spotify.com/api/token`)
- `SPOTIFY_API_BASE_URL` (default: `https://api.spotify.com/v1`)
- `SPOTIFY_TIMEOUT_MS`
- `SPOTIFY_CONNECT_TIMEOUT_MS` (default: `2000`)
- `SPOTIFY_READ_TIMEOUT_MS` (default: `120000`)
- `SPOTIFY_WRITE_TIMEOUT_MS` (default: `30000`)

Run a redacted live Spotify check from `AuraBeat/backend`:

```sh
npm run test:spotify
```

### AI workflow

`POST /recommendations/analyze` extracts mood metadata.

`POST /recommendations/generate` performs full mood-to-playlist generation.

Spotify lookup/hydration is intentionally marked as a later integration step in the playlist generation service.
