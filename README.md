# Unfiltered

Unfiltered is a modern Android application designed for emotional well-being, featuring interactive journaling, real-time community chat, and location-based "Detox" suggestions. Built with **Jetpack Compose** and a **Node.js/PostgreSQL** backend.

## 🚀 Features

- **Real-time Sanctuaries**: Interactive chat rooms built with **Socket.io** for real-time community support.
- **Detox & Explore**: Location-aware suggestions for parks, cafes, and gyms using **Google Maps & Places API** to help users disconnect and recharge.
- **Dynamic Journaling**: Expressive mood logging and journaling with a rich, modern UI.
- **Secure Authentication**: Full registration and login flow with token-based security.
- **Premium UI**: Crafted with Material 3, custom animations, and a sleek dark-themed design system.

## 🛠️ Setup & Installation

To run this project locally, you need to set up your environment variables and API keys.

### 1. Google Maps API Key
This project uses the **Secrets Gradle Plugin** to keep API keys secure.
1. Create a `local.properties` file in the root directory (if it doesn't exist).
2. Add your Google Maps API key:
   ```properties
   MAPS_API_KEY=YOUR_API_KEY_HERE
   ```

### 2. Backend Configuration
Ensure you have the [Unfiltered Backend](https://github.com/vidney14/UnfilteredApp-Backend) running.
Update the `BASE_URL` in `NetworkConstants.kt` if your backend is hosted non-locally:
```kotlin
const val BASE_URL = "http://10.0.2.2:3000/" // Default for Android Emulator
```

## 🏗️ Architecture

- **UI**: Jetpack Compose (100% Kotlin)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit & OkHttp
- **Real-time**: Socket.io
- **Dependency Management**: Gradle Version Catalog (libs.versions.toml)
- **Local Persistence**: SharedPreferences (Auth Tokens)

## 📸 Screenshots

<p align="center">
  <img width="250" alt="Signup" src="https://github.com/user-attachments/assets/45d6ef45-a7eb-478a-acf0-d9baaa12e305" />
  <img width="250" alt="Mood" src="https://github.com/user-attachments/assets/15c872f1-e783-4264-8957-806a948f48be" />
  <img width="250" alt="Rooms" src="https://github.com/user-attachments/assets/bfb9d27e-a2b5-4d1c-913b-0ec2fd656a90" />
</p>

## 🤖 Responsible Use of AI

AI tools were utilized to accelerate development in the following ways:
- **Architecture**: Refining MVVM patterns and StateFlow management.
- **Animations**: Implementing smooth transitions and micro-interactions in Compose.
- **Security**: Implementing best practices for API key management and authentication flows.
- **Problem Solving**: Debugging WebSocket connectivity and complex UI layouts.

*All AI-generated code was reviewed, debugged, and integrated manually to ensure project integrity.*
