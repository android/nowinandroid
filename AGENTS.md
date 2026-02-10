# AGENTS.md

This file provides guidance to AI coding agents when working with code in this repository.

## Project Overview

Now in Android (KMP edition) — a Kotlin Multiplatform fork of Google's Now in Android reference app. Targets Android, iOS, Desktop (JVM), macOS, and experimental Web (WASM). Uses Compose Multiplatform for shared UI.

## Build Commands

```bash
# Format code (must pass before merge)
./gradlew spotlessApply
./gradlew spotlessCheck --init-script gradle/init.gradle.kts --no-configuration-cache

# Build
./gradlew :app:assemble                     # main app (all variants)
./gradlew :app-nia-catalog:assemble          # design system catalog app

# Run desktop app
# Gradle run config task: desktopRun -DmainClass=MainKt --quiet

# Unit tests (only demoDebug variant has test coverage)
./gradlew testDemoDebug                      # all module tests
./gradlew :feature:foryou:testDemoDebug      # single module test
./gradlew :lint:test                         # lint rule tests

# Instrumented tests (requires a connected Android emulator)
./gradlew connectedDemoDebugAndroidTest

# Screenshot tests (Roborazzi — CI only, do NOT run locally)
# CI records/verifies screenshots and auto-commits updates via PR
# ./gradlew verifyRoborazziDemoDebug         # verify against baselines (CI only)
# ./gradlew recordRoborazziDemoDebug         # record new baselines (CI only)

# Lint
./gradlew :app:lintProdRelease :app-nia-catalog:lintRelease :lint:lint

# Dependency guard
./gradlew dependencyGuard                    # check
./gradlew dependencyGuardBaseline            # update baseline

# Build-logic check
./gradlew :build-logic:convention:check

# Badging check
./gradlew :app:checkProdReleaseBadging
```

## Architecture

**Three-layer architecture** with unidirectional data flow (UDF):
- **UI Layer** (`feature:*`) — Compose screens + ViewModels. Each feature module is independent; no inter-feature dependencies.
- **Domain Layer** (`core:domain`) — Use cases combining data from multiple repositories.
- **Data Layer** (`core:data`, `core:database`, `core:datastore`, `core:network`) — Offline-first repositories backed by SQLDelight + Proto DataStore, synced via Ktor/Ktrofit.

### Module Structure

- **`app`** — Main KMP application with app-level navigation (NiaNavHost, TopLevelDestination). WIP for full multiplatform.
- **`app-nia-catalog`** — Standalone design system showcase. Fully multiplatform including WASM.
- **`feature:*`** — Feature modules (foryou, interests, bookmarks, topic, search, settings). Depend only on `core:*` modules.
- **`core:*`** — Shared libraries. Key ones: `designsystem` (Material 3 components), `ui` (reusable composables), `model` (data types), `data` (repositories), `database` (SQLDelight), `network` (Ktor), `datastore` (Proto DataStore).
- **`shared`** — Aggregates features and core for the main app.
- **`sync:work`** — WorkManager-based sync (Android-only).
- **`build-logic/convention`** — Custom Gradle convention plugins.

### Convention Plugins

All modules use convention plugins from `build-logic/convention/` (plugin IDs prefixed `nowinandroid.*`):
- `nowinandroid.kmp.library` — KMP library: configures Android library + KMP targets (JVM, Android, iOS, macOS) + common test deps
- `nowinandroid.cmp.application` — Compose Multiplatform app (Android, Desktop, iOS, macOS)
- `nowinandroid.cmp.feature` — Feature module (inherits kmp.library + adds Compose, Koin, core:ui, core:designsystem)
- `nowinandroid.di.koin` — Koin DI with KSP annotation processing
- `nowinandroid.sqldelight` — SQLDelight database
- `nowinandroid.kmp.inject` — kotlin-inject DI via KSP

### Key Technology Choices

- **DI:** Koin (primary, KMP-friendly service locator) + kotlin-inject (compile-time alternative)
- **Database:** SQLDelight (multiplatform)
- **Preferences:** Proto DataStore
- **Networking:** Ktor client via Ktrofit
- **Image loading:** Coil 3
- **Serialization:** kotlinx-serialization
- **Testing:** No mocking libraries — use test doubles. Roborazzi for screenshot tests (CI only), Turbine for Flow testing.

### Build Variants

Two flavor dimensions: `demo` (local static data) and `prod` (requires backend server, not public). Use `demoDebug` for development.

### KMP Source Set Layout

Modules use Kotlin Multiplatform default hierarchy: `commonMain`/`commonTest`, `androidMain`/`androidUnitTest`/`androidInstrumentedTest`, `jvmMain`/`jvmTest`, `iosMain`, `macosMain`, etc. Android resource prefixes are auto-derived from module path (e.g., `core_model_`).

## Requirements

- JDK 17+
- Gradle uses 4GB heap (`-Xmx4g`)
- Configuration cache and parallel builds enabled
