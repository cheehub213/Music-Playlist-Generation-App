# API

## AI Recommendation Workflow

The AI pipeline is backend-only and follows this sequence:

1. Client sends a mood prompt to AuraBeat backend.
2. Backend calls Groq API with a strict JSON schema instruction.
3. Backend extracts mood, emotions, genres, energy, vibe, and keywords.
4. Backend generates a playlist recommendation payload.
5. Backend returns structured JSON to Android.

No AI provider API key is exposed to Android or web clients.

## Endpoints

### POST /recommendations/analyze

Request body:

```json
{
	"prompt": "I want something nostalgic and calm"
}
```

Response example:

```json
{
	"mood": "nostalgic",
	"emotions": ["reflective", "warm"],
	"genres": ["lo-fi", "indie"],
	"energy": "medium",
	"vibe": "midnight city lights",
	"keywords": ["nostalgic", "calm", "warm"],
	"playlistTitle": "Midnight Nostalgia",
	"source": "groq"
}
```

### POST /recommendations/generate

Request body:

```json
{
	"prompt": "Give me low-energy rainy focus music"
}
```

Response example:

```json
{
	"userId": "u-123",
	"inputPrompt": "Give me low-energy rainy focus music",
	"generatedAt": "2026-05-16T11:22:33.000Z",
	"mood": "focused",
	"emotions": ["calm", "intentional"],
	"genres": ["lo-fi", "ambient"],
	"energy": "medium",
	"vibe": "rainy study session",
	"keywords": ["rain", "focus", "study"],
	"playlistTitle": "Rain Window Focus",
	"playlist": {
		"id": "generated-1715858553000",
		"title": "Rain Window Focus",
		"description": "A rainy study session mix tuned for a medium energy level.",
		"genres": ["lo-fi", "ambient"],
		"energyScore": 0.6,
		"tracks": [
			{ "id": "seed-1", "title": "lo-fi pulse 1", "artist": "AuraBeat AI Seed", "source": "seed" }
		],
		"spotifySyncStatus": "pending"
	}
}
```

## Axios Example (Backend/Server-to-Server)

```js
const axios = require("axios");

async function generateMoodPlaylist(token, prompt) {
	const response = await axios.post(
		"http://localhost:3000/recommendations/generate",
		{ prompt },
		{
			headers: {
				Authorization: `Bearer ${token}`,
				"Content-Type": "application/json",
			},
			timeout: 12000,
		}
	);

	return response.data;
}
```

## Retrofit Example (Android)

```kotlin
interface AuraBeatApiService {
		@POST("recommendations/generate")
		suspend fun generateMoodPlaylist(@Body request: MoodPromptRequest): GroqRecommendationResponse
}

data class MoodPromptRequest(val prompt: String)
```
