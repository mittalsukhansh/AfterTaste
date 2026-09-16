# AfterTaste ☕

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.2-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-brightgreen.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Database-Room%20KSP-orange.svg)](https://developer.android.com/training/data-storage/room)

**AfterTaste** is an artisanal coffee passport, journal, and location-based discovery app built natively for Android using Jetpack Compose. Track every cafe visit, rate coffee beans & ambience, discover nearby roasters on an interactive map, and build your digital coffee passport.

---

## ✨ Features

### 🎨 Pinterest-Style Staggered Grid UI
- **Masonry Layout**: Displays cafes, visit logs, and photos in a `LazyVerticalStaggeredGrid`.
- **Fluid Animations**: Scale press micro-interactions (`graphicsLayer`), spring entrance animations, and adaptive aspect ratio cards.
- **Warm Coffee Theme**: Signature parchment cream background (`#F4ECE1`), deep espresso typography (`#2A1810`), and terracotta highlights (`#C05A3E`).

### 🖋️ Brand Identity & Custom Typography
- **Custom Title Font**: Stylized brush script typography matching the AfterTaste brand logo across headers, top bars, and onboarding.
- **Adaptive Launcher Icon**: Custom vector and high-density PNG adaptive launcher icons for squircle, round, and legacy home screens.

### 📍 Dynamic Nearby Cafe Map
- **Live Location Detection**: Dynamically calculates nearby artisanal coffee spots relative to the user's real GPS position.
- **Interactive Pins & Callouts**: Distance metrics (*e.g., "0.3 km away"*), filter chips (*"Nearby Cafes 📍"*, *"Visited ☕"*, *"Wishlist ♡"*), and quick action callout cards.

### 👤 Google Account & Cloud Sync
- **Google Sign-In**: Integrated profile card displaying user avatar, name, and email in Settings & Dashboard.
- **Cloud Backup State**: Automatic timestamped sync indicator for backing up coffee visits and wishlist items safely.

### 📊 Passport Stamps & Analytics
- **Achievement Stamps**: Unlock digital passport badges (*"Espresso Explorer"*, *"Cozy Regular"*, *"WiFi Nomad"*) based on visit milestones.
- **Coffee Journal Analytics**: Track total spend, monthly visits, favorite cafe stats, and active weekly streaks.

### 💾 Data Export & Restore
- **CSV & JSON Export**: Export your complete coffee journal to spreadsheet CSV or full JSON backup files.
- **Restore & Backup**: Restore data seamlessly from JSON backups or share exported data via Android share sheet.

---

## 🛠 Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/) (2.2.10)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Local Database**: [Room 2.6](https://developer.android.com/training/data-storage/room) with [KSP](https://kotlinlang.org/docs/ksp-overview.html)
- **Asynchronous Flow**: Kotlin Coroutines & `StateFlow`
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Build System**: Android Gradle Plugin 9.3+ with Kotlin DSL (`build.gradle.kts`) and Version Catalogs (`libs.versions.toml`)

---

## 🚀 Getting Started

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

## 📄 License

```text
Copyright 2026 AfterTaste App. All rights reserved.
```
