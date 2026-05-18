# MidnightVPN

A premium Android VPN client with a dark, cyberpunk-inspired UI. Automatically discovers free VPN servers via the VPN Gate API and connects using OpenVPN-style configurations.

## Features

- **Auto-discovery**: Fetches live server lists from VPN Gate API
- **Real-time ping sorting**: Servers sorted by latency for optimal performance
- **Premium UI**: Glassmorphism cards, neon glow effects, pulsing animations
- **Kill Switch**: Blocks internet if VPN drops
- **Split Tunneling**: Exclude specific apps from VPN
- **Real-time stats**: Live download/upload speed and public IP display

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Network**: Retrofit + OkHttp
- **Persistence**: DataStore Preferences
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Building

```bash
./gradlew assembleDebug
```

## Project Structure

```
com.midnight.vpn/
├── data/
│   ├── remote/api/       # Retrofit API interfaces
│   ├── remote/dto/       # Data parsers (VPN Gate CSV)
│   └── repository/       # Repository implementations
├── di/                   # Hilt dependency injection modules
├── domain/
│   ├── model/            # Data classes (VpnServer, ConnectionState, etc.)
│   ├── repository/       # Repository interfaces
│   └── usecase/          # Use cases
├── service/              # Android VpnService implementation
├── ui/
│   ├── components/       # Reusable Compose components
│   ├── navigation/       # Navigation graph
│   ├── screens/          # Screen composables + ViewModels
│   └── theme/            # Colors, Typography, Glassmorphism modifiers
└── util/                 # Formatting utilities
```
