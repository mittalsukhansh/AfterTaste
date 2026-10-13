# AfterTaste

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Release](https://img.shields.io/badge/Release-v2.0.0-orange.svg)](https://github.com/mittalsukhansh/AfterTaste/releases/tag/v2.0.0)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.2-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Database-Room%20KSP-orange.svg)](https://developer.android.com/training/data-storage/room)
[![Map](https://img.shields.io/badge/Maps-OpenStreetMap%20%2F%20OSMDroid-success.svg)](https://www.openstreetmap.org)

**AfterTaste** is an artisanal coffee passport, journal, and location-based discovery app built natively for Android using Jetpack Compose with an interactive web companion. Track every cafe visit, rate coffee beans & ambience, discover nearby roasters on an open-source map, and build your digital coffee passport.

---

## Download & Releases

| Version | Release Date | Download APK | Release Notes |
|:---|:---|:---|:---|
| **v2.0.0** (Latest) | October 2026 | [Download AfterTaste-v2.0.0.apk](https://github.com/mittalsukhansh/AfterTaste/releases/download/v2.0.0/AfterTaste-v2.0.0.apk) | [Changelog](https://github.com/mittalsukhansh/AfterTaste/releases/tag/v2.0.0) |
| **v1.0.1** | October 2026 | [Download AfterTaste-v1.0.1.apk](https://github.com/mittalsukhansh/AfterTaste/releases/download/v1.0.1/AfterTaste-v1.0.1.apk) | [Changelog](https://github.com/mittalsukhansh/AfterTaste/releases/tag/v1.0.1) |
| **v1.0.0** | October 2026 | [Download AfterTaste-v1.0.0.apk](https://github.com/mittalsukhansh/AfterTaste/releases/download/v1.0.0/AfterTaste-v1.0.0.apk) | [Changelog](https://github.com/mittalsukhansh/AfterTaste/releases/tag/v1.0.0) |

> **Direct APK Installation**: Download `AfterTaste-v2.0.0.apk` directly on your Android device (Android 7.0+ / API 24+) and install. Ensure "Install unknown apps" permission is enabled in your device settings.

---

## Features

### Pinterest-Style Staggered Grid UI
- **Masonry Layout**: Displays cafes, visit logs, and photos in a `LazyVerticalStaggeredGrid`.
- **Fluid Animations**: Scale press micro-interactions (`graphicsLayer`), spring entrance animations, and adaptive aspect ratio cards.
- **Warm Coffee Theme**: Signature parchment cream background (`#F4ECE1`), deep espresso typography (`#2A1810`), and terracotta highlights (`#C05A3E`).

### Brand Identity & Custom Typography
- **Custom Title Font**: Stylized brush script typography matching the AfterTaste brand logo across headers, top bars, and onboarding.
- **Adaptive Launcher Icon**: Custom vector and high-density PNG adaptive launcher icons for squircle, round, and legacy home screens.

### 100% Free Open-Source Map Engine
- **Zero Proprietary API Keys**: Built entirely on **OpenStreetMap (OSM)**, **osmdroid**, and **Leaflet.js** without Google Cloud billing dependencies.
- **Overpass API Discovery**: Queries real-time specialty cafes within 5 km of GPS coordinates.
- **Auto-Centering & GPS Pin**: Smooth fly-to centering on user location with pulsating radar marker.

### Interactive Barista Brew Guide & Timer
- **Multi-Method Extraction**: Dedicated stage timers for V60 Pour Over, AeroPress, French Press, and Moka Pot.
- **Smart Ratio Calculator**: Automatically computes water yield in grams and ideal brew temperature.
- **One-Tap Extraction Transfer**: Saves extraction time and water ratio straight to the coffee cupping log.

### Shareable Cupping Story Card Generator
- **Aesthetic 9:16 Story Cards**: Generates high-res Polaroid story cards for Instagram & WhatsApp.
- **HTML5 Canvas PNG Download**: Exports 1080×1920 PNG cards with flavor chips, ratings, and custom watermark.

### Coffee Spend Analytics & Monthly Budget Tracker (₹ INR)
- **Rupee Budget Tracker**: Tracks monthly spend in ₹ with visual budget progress.
- **Cost-Per-Cup Metrics**: Compares cafe visits against home brewing with calculated savings.

### Coffee Bag Label Scanner (Neural OCR)
- **Instant Label Import**: Camera/file upload with automated extraction of roaster, origin lot, process, and tasting notes.

### Google Account & Cloud Sync
- **Google Sign-In**: Profile management with timestamped cloud backup state.

### Passport Milestones & Streaks
- **Gamified Achievements**: Unlock Origin Explorer, Anaerobic Master, and Roastery Nomad tiers.
- **Hopping Streaks**: Track consecutive weekend cafe cupping streaks.

### Data Export & Restore
- **CSV & JSON Export**: Export your complete coffee journal to spreadsheet CSV or full JSON backup files.
- **Restore & Backup**: Restore data seamlessly from JSON backups or share exported data via Android share sheet.

---

## Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/) (2.2.10)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Local Database**: [Room 2.6](https://developer.android.com/training/data-storage/room) with [KSP](https://kotlinlang.org/docs/ksp-overview.html)
- **Asynchronous Flow**: Kotlin Coroutines & `StateFlow`
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Build System**: Android Gradle Plugin 9.3+ with Kotlin DSL (`build.gradle.kts`) and Version Catalogs (`libs.versions.toml`)

---

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17+
- Android SDK 35

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/mittalsukhansh/AfterTaste.git
   cd AfterTaste
   ```

2. Open the project in Android Studio.

3. Sync Gradle dependencies:
   ```bash
   ./gradlew sync
   ```

4. Build and run debug build on device/emulator:
   ```bash
   ./gradlew app:assembleDebug
   ```

---

## License

```text
Copyright 2026 AfterTaste App. All rights reserved.
```
