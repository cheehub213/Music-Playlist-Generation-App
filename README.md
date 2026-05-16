<<<<<<< HEAD
# AuraBeat

AuraBeat is an AI-powered music streaming mobile application inspired by Spotify, designed to create personalized music experiences based on the user’s emotions and mood.

## Summary
Users describe how they feel using natural language prompts (e.g. “I feel nostalgic tonight”, “Relaxing music for studying”, “Energetic gym vibes”). AuraBeat analyzes the mood and generates personalized playlists that match the requested atmosphere.

The project is a monorepo that contains the Android client and the Node.js backend.

## Structure
- `mobile/` - Android app
- `backend/` - Node.js API
- `docs/` - Project documentation

## Features
- Modern responsive UI inspired by Spotify
- Dark/Light mode support
- Home, Search, Library, and Profile screens
- Full music player interface
- Artist details pages
- AI playlist generation workflow
- Mock backend simulation

## Architecture & Tech
- Kotlin, Jetpack Compose, Material 3
- MVVM architecture, StateFlow, Repository pattern
- Backend: Node.js + Express

## Local development
Start the backend first, then run the Android app:

```bash
cd backend
npm install
npm run dev
```

Verify `http://127.0.0.1:3000/health` returns `{"status":"ok"}`. Android emulators reach the host through `http://10.0.2.2:3000/`.

## Planned Features
- Spotify API integration
- OpenAI/GROQ-powered mood analysis
- Real-time playlist generation and persistence
- Offline listening and recommendations

## Notes
This repository separates provider secrets from the client: the Android app never stores provider secrets; the backend owns credentials through environment variables.
