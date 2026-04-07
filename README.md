**UPDATE 1**

## Core App Functionality & Compose UI
The app is developed using Jetpack Compose with multiple screens such as Journal, Mood, Music, Rooms, and Detox, all connected through Navigation Compose. It uses Scaffold for overall layout structure and LazyColumn for displaying dynamic journal entries efficiently. Material 3 components such as NavigationBar, Cards, Buttons, and OutlinedTextField help create a clean, modern, and user-friendly UI.

## State Management, ViewModel, and Architecture
The application follows a basic MVVM architecture, where ViewModels such as JournalViewModel and MoodViewModel manage UI-related data. State is handled using StateFlow and observed in composables through collectAsState(), ensuring reactive UI updates. This creates a clear separation between UI and logic, improving maintainability and scalability.

## Navigation & App Flow
Navigation is implemented using Navigation Compose with a sealed Screen class for type-safe routing. The app provides a smooth and logical flow from authentication screens to core features, with arguments such as mood type passed between screens where needed. A bottom navigation bar enables intuitive and seamless switching between the main sections of the app.

## Q1.4 Integration Progress Toward Requirements

### API Integration
Basic backend API integration has been implemented using REST endpoints for authentication. The app communicates with endpoints such as `/register`, `/login`, and `/me`, establishing a working foundation for user authentication and data exchange.

### Data Persistence Plan / Early Implementation
The project uses PostgreSQL as the primary database for storing user and application data. A structured backend setup is in place, providing a scalable foundation for persistent data handling.

### Sensor Integration
The application uses the device microphone as a sensor for voice-based journaling. Permission handling for audio recording has been implemented, and basic functionality is in place, with future plans for full voice-to-text integration.

### Location / Maps
Google Maps integration has been initiated using a static map API to display location-based content. Basic map rendering is implemented, serving as a foundation for future interactive and location-aware features.


## Screenshots 
<img width="300" height="600" alt="Screenshot_20260407_154250" src="https://github.com/user-attachments/assets/45d6ef45-a7eb-478a-acf0-d9baaa12e305" />

<img width="300" height="600" alt="Screenshot_20260407_144501" src="https://github.com/user-attachments/assets/15c872f1-e783-4264-8957-806a948f48be" />

<img width="300" height="600" alt="Screenshot_20260407_144412" src="https://github.com/user-attachments/assets/bfb9d27e-a2b5-4d1c-913b-0ec2fd656a90" />
<img width="300" height="600" alt="Screenshot_20260407_153455" src="https://github.com/user-attachments/assets/9e5f83d9-da3d-4743-90a3-47106f1c3de3" />
<img width="300" height="600" alt="Screenshot_20260407_153503" src="https://github.com/user-attachments/assets/f7571260-bff4-4c8f-84c1-41a62021ce36" />
<img width="300" height="600" alt="Screenshot_20260407_153508" src="https://github.com/user-attachments/assets/4b7d2127-ec19-4d74-a24c-4a4c846174d2" />
