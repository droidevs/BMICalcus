# BMI Calc

A modern Android BMI calculator with history tracking, progress charts, and goal management, built with Jetpack Compose.

## Features
1. **BMI calculation** with metric/imperial inputs and validation.
2. **History & record management** with details, edit flow, and favorites support.
3. **Charts** for BMI/height/weight over time with range selection.
4. **Goal setup** with progress visualization and timeline.
5. **Responsive UI** for phones, tablets, and foldables (drawer, rail, bottom nav).

## Tech stack
- **Kotlin**, Coroutines, Flow
- **Jetpack Compose** + Material 3
- **Navigation Compose**
- **Hilt** for dependency injection
- **Room** for local storage
- **DataStore** for preferences
- **KSP**, **Kotlinx Serialization**, **Window Manager**

## Project structure
- `app/src/main/java/io/droidevs/bmicalc/data` — Room, DataStore, repositories
- `app/src/main/java/io/droidevs/bmicalc/domain` — models, use cases, result types
- `app/src/main/java/io/droidevs/bmicalc/ui` — Compose UI, viewmodels, navigation

## Getting started
**Prerequisites**
- Android Studio (with Android SDK installed)
- JDK 17
- Android SDK 34+ (compileSdk 35, targetSdk 34)

**Setup**
1. Open the project in Android Studio.
2. Let Android Studio generate `local.properties` (it should point to your SDK).
3. Sync Gradle and run the app on a device/emulator.

## Build & run (CLI)
**Windows (PowerShell)**
```ps1
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

**macOS/Linux**
```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

**Tests**
```bash
./gradlew test
```

## Notes
- `gradle.properties` currently pins `org.gradle.java.home` to the Android Studio JBR on Windows.  
  If your JDK path differs (or you’re on macOS/Linux), update that value to your local JDK 17 path.
