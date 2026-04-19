# 🌿 Unfiltered

**Unfiltered** is a modern Android application designed for emotional well-being. It helps users track their moods, journal their thoughts, connect with others in real-time community chat rooms, discover music based on how they feel, and find nearby places for a digital detox — all wrapped in a sleek, dark-themed UI.

Built entirely with **Jetpack Compose** (100% Kotlin) and backed by a **Node.js / PostgreSQL** REST + WebSocket API.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🎭 **Mood Wheel** | A comprehensive wheel with 100+ mood sub-types across 4 energy quadrants. Users select their current mood each session. |
| 📊 **Mood Analytics** | Visual analytics dashboard showing mood logs over the past 7 days — total logs, mood distribution, and daily breakdowns. |
| 📓 **Journal** | A personal journal where users can write and view past entries, all stored securely in the backend. |
| 🎵 **Music** | Spotify-powered music recommendations that match the user's current mood using the Spotify Web API. |
| 💬 **Sanctuaries (Chat)** | Real-time community chat rooms powered by **Socket.io**. Rooms are tagged by mood for contextual conversations. |
| 🗺️ **Detox & Explore** | Location-based "digital detox" feature. Uses **Google Maps** + **Places API** to show nearby parks, cafes, gyms, and restaurants based on the user's current location. |
| 🔐 **Auth** | Full email/password registration and login with **JWT** token-based authentication. Auto-login on app restart via persisted token. |

---

## 🛠️ Tech Stack

### Android
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM (ViewModel + StateFlow + Repository pattern)
- **Navigation**: Jetpack Navigation Compose (type-safe routes via `@Serializable`)
- **Networking**: Retrofit 2 + OkHttp + kotlinx.serialization
- **Real-time**: Socket.io client (`io.socket:socket.io-client:2.1.0`)
- **Maps**: Google Maps Compose + Play Services Maps & Location
- **Music**: Spotify Web API (Client Credentials flow)
- **Image Loading**: Coil
- **DI / Build**: Gradle Version Catalog (`libs.versions.toml`) + Secrets Gradle Plugin
- **Min SDK**: 24 (Android 7.0+) | **Target SDK**: 35

### Backend
- **Runtime**: Node.js
- **Database**: PostgreSQL (hosted on [Neon](https://neon.tech))
- **Hosting**: [Render](https://render.com) — `https://unfilteredapp-backend.onrender.com`
- **Real-time**: Socket.io
- **Auth**: JWT

---

## 📁 Project Structure

```
UnfilteredApp/
├── app/src/main/java/com/example/unfilteredapp/
│   ├── MainActivity.kt              # Single Activity — sets up nav graph + bottom bar
│   ├── data/
│   │   ├── api/
│   │   │   ├── NetworkConstants.kt  # BASE_URL, SOCKET_URL, MAPS_API_KEY
│   │   │   ├── AuthApi.kt           # /auth/register, /auth/login, /auth/me
│   │   │   ├── ChatApi.kt           # /rooms, /rooms/:id/messages
│   │   │   ├── JournalApi.kt        # /journal
│   │   │   ├── MoodApi.kt           # /mood/log, /mood/analytics
│   │   │   ├── PlacesApi.kt         # Google Places Nearby Search
│   │   │   ├── SpotifyApi.kt        # Spotify /v1/search, /v1/recommendations
│   │   │   └── SpotifyAuthApi.kt    # Spotify /api/token (Client Credentials)
│   │   ├── model/
│   │   │   ├── AuthModels.kt        # AuthRequest, RegistrationRequest, AuthResponse, User
│   │   │   ├── ChatModels.kt        # Room, Message (handles both snake_case & camelCase)
│   │   │   ├── JournalModels.kt     # JournalEntryRequest, JournalEntryResponse
│   │   │   ├── MoodData.kt          # 100+ MoodSubType entries across 4 quadrants
│   │   │   ├── MoodAnalyticsModels.kt # MoodLogRequest, AnalyticsResponse, DailyLog
│   │   │   ├── PlaceData.kt         # PlacesResponse, PlaceResult
│   │   │   └── SpotifyModels.kt     # SpotifyTrack, SpotifySearchResponse
│   │   └── repository/
│   │       ├── AuthRepository.kt    # Login, register, token persist (SharedPreferences)
│   │       ├── ChatRepository.kt    # Fetch rooms & messages
│   │       ├── JournalRepository.kt # Get/save journal entries
│   │       ├── MoodRepository.kt    # Log mood, get analytics
│   │       ├── PlacesRepository.kt  # Nearby places search via Google API
│   │       └── SpotifyRepository.kt # Client Credentials token + track search
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── SplashScreen.kt      # Animated splash → auto-login check
│   │   │   ├── LoginScreen.kt       # Email + password login
│   │   │   ├── SignupScreen.kt      # Name, email, password registration
│   │   │   ├── MoodCategoryScreen.kt# 4-quadrant mood energy selector
│   │   │   ├── MoodSubSelectionScreen.kt # Granular mood picker
│   │   │   ├── MoodSummaryScreen.kt # Summary + navigation after mood pick
│   │   │   ├── AnalyticsScreen.kt   # 7-day mood analytics charts
│   │   │   ├── JournalScreen.kt     # Write and view journal entries
│   │   │   ├── MusicScreen.kt       # Spotify mood-based track recommendations
│   │   │   ├── RoomsScreen.kt       # List of mood-tagged chat rooms
│   │   │   ├── ChatScreen.kt        # Real-time Socket.io chat
│   │   │   └── DetoxScreen.kt       # Google Maps + nearby place categories
│   │   └── theme/                   # Material 3 color scheme, typography
│   └── viewmodel/
│       ├── AuthViewModel.kt         # Login/register/logout + auto-login
│       ├── ChatViewModel.kt         # Socket.io lifecycle + optimistic UI
│       ├── JournalViewModel.kt      # Fetch/add journal entries
│       ├── MoodAnalyticsViewModel.kt# Log mood + fetch analytics
│       ├── MoodViewModel.kt         # In-memory selected mood state
│       ├── MusicViewModel.kt        # Spotify track loading by mood
│       └── PlacesViewModel.kt       # Nearby places via Google Places API
└── gradle/
    └── libs.versions.toml           # Centralized dependency version catalog
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| Android Studio | Ladybug (2024.2.x) or newer |
| JDK | 11+ |
| Android SDK | API 35 |
| A physical device or emulator | API 24+ |

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/vidney14/UnfilteredApp.git
cd UnfilteredApp
```

---

### Step 2 — Configure `local.properties`

This file lives in the root of the project and is **never committed to Git**. Create it if it doesn't exist, or add the following keys to the existing file:

```properties
# Android SDK path (auto-generated by Android Studio)
sdk.dir=/Users/<your-username>/Library/Android/sdk

# Google Maps API Key
# Get one from: https://console.cloud.google.com → Maps SDK for Android + Places API
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY

# Spotify API credentials
# Get from: https://developer.spotify.com/dashboard → Create an App
SPOTIFY_CLIENT_ID=YOUR_SPOTIFY_CLIENT_ID
SPOTIFY_CLIENT_SECRET=YOUR_SPOTIFY_CLIENT_SECRET
```

> **Important:** Enable **Maps SDK for Android** AND **Places API (New)** for your Google API key in the Google Cloud Console.

---

### Step 3 — Backend URL

The app points to the live production backend by default. You can verify or change this in:

**`app/src/main/java/com/example/unfilteredapp/data/api/NetworkConstants.kt`**
```kotlin
object NetworkConstants {
    const val BASE_URL = "https://unfilteredapp-backend.onrender.com/"
    val SOCKET_URL = BASE_URL.removeSuffix("/")      // For Socket.io
    const val GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com/maps/api/"
    val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
}
```

To run a **local backend**, replace `BASE_URL` with your machine's IP (e.g., `http://10.0.2.2:3000/` for the Android emulator, or `http://<your-local-ip>:3000/` for a physical device).

> 📦 Backend repository: [UnfilteredApp-Backend](https://github.com/vidney14/UnfilteredApp-Backend)

---

### Step 4 — Open in Android Studio

1. Open **Android Studio**
2. Click **File → Open** and select the `UnfilteredApp` folder
3. Wait for Gradle to sync (this downloads all dependencies automatically)
4. If prompted, click **Sync Now**

---

### Step 5 — Run the App

1. Connect a physical Android device via USB (enable USB debugging) **or** launch an emulator (API 24+)
2. Select your device in the device dropdown at the top of Android Studio
3. Click the ▶ **Run** button (or press `Shift + F10`)

The app will build and launch. If it's your first run, you'll be taken to the **Signup** screen.

---

## 🔑 Authentication Flow

```
App Launch
    └── SplashScreen (animated logo)
            ├── Token found in SharedPreferences → MoodCategoryScreen (auto-login)
            └── No token → LoginScreen
                    └── Signup → LoginScreen → MoodCategoryScreen
```

- JWT token is stored in `SharedPreferences` under the key `jwt_token`
- All authenticated API requests include `Authorization: Bearer <token>` via an OkHttp interceptor
- Logout clears the token and navigates back to `LoginScreen`

---

## 🗺️ Navigation Graph

The app uses a **single-activity, type-safe** navigation graph:

| Route | Screen |
|---|---|
| `Splash` | Animated splash screen |
| `Login` | Login form |
| `Signup` | Registration form |
| `MoodCategory` | 4-quadrant mood energy home screen |
| `MoodSubSelection(modeType)` | Granular mood selection |
| `MoodSummary` | Post-mood-selection summary |
| `Journal` | Personal journal |
| `Music` | Spotify mood music |
| `Rooms` | Chat room list |
| `Chat(roomId, roomName, moodTag, description)` | Live chat room |
| `Detox` | Google Maps detox explore |
| `Analytics` | Mood analytics dashboard |

The **bottom navigation bar** shows 5 tabs: **Journal**, **Music**, **Mood**, **Rooms**, **Detox**. It is hidden on `Login`, `Signup`, `Chat`, and `Splash` screens.

---

## 🔌 Key Integrations

### Spotify (Music Screen)
- Uses the [Client Credentials Flow](https://developer.spotify.com/documentation/web-api/tutorials/client-credentials-flow) — no user login required
- Token is fetched from `https://accounts.spotify.com/api/token` and cached in memory with expiry
- Tracks are searched via `/v1/search` based on mood-mapped keywords

### Google Maps & Places (Detox Screen)
- Uses `maps-compose` for the embedded map view
- Requests `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions at runtime
- Nearby places are fetched via `https://maps.googleapis.com/maps/api/place/nearbysearch/json`
- Categories: Parks, Cafes, Gyms, Restaurants

### Socket.io (Chat Screen)
- Connects to `SOCKET_URL` using `polling` → `websocket` transports with auto-reconnect
- Events: `join_room`, `send_message`, `receive_message`
- Optimistic UI: messages appear instantly (id = null) and are replaced when the server confirms

---

## 📦 Key Dependencies

| Library | Purpose |
|---|---|
| `androidx.navigation:navigation-compose` | Type-safe in-app navigation |
| `retrofit2:retrofit` | HTTP client |
| `com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter` | JSON serialization |
| `com.squareup.okhttp3:logging-interceptor` | Network logging |
| `io.socket:socket.io-client:2.1.0` | Real-time WebSocket via Socket.io |
| `com.google.maps.android:maps-compose` | Google Maps in Compose |
| `com.google.android.gms:play-services-maps` | Google Maps SDK |
| `com.google.android.gms:play-services-location` | GPS location |
| `io.coil-kt:coil-compose` | Async image loading |
| `com.google.android.libraries.mapsplatform.secrets-gradle-plugin` | Secure API key management |

---

## 📸 Screenshots

<p align="center">
  <img width="250" alt="Signup" src="https://github.com/user-attachments/assets/45d6ef45-a7eb-478a-acf0-d9baaa12e305" />
  <img width="250" alt="Mood" src="https://github.com/user-attachments/assets/15c872f1-e783-4264-8957-806a948f48be" />
  <img width="250" alt="Rooms" src="https://github.com/user-attachments/assets/bfb9d27e-a2b5-4d1c-913b-0ec2fd656a90" />
</p>

---

## 🤖 Responsible Use of AI

AI tools were used to accelerate development in the following areas:

- **Architecture**: Refining MVVM patterns and StateFlow management
- **Animations**: Implementing smooth transitions and micro-interactions in Compose
- **Security**: Best practices for API key management and authentication flows
- **Problem Solving**: Debugging WebSocket connectivity, duplicate message handling, and complex UI layouts

*All AI-generated code was reviewed, debugged, and integrated manually to ensure correctness and project integrity.*
