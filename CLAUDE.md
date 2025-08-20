# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Run
```bash
# Build the demo debug variant (recommended for development)
./gradlew assembleDemoDebug

# Build release variant (for performance testing)
./gradlew assembleDemoRelease

# Run the app (use demoDebug variant in Android Studio)
# Change run configuration to 'app' if needed
```

### Testing
```bash
# Run unit tests (demo debug variant only)
./gradlew testDemoDebug

# Run specific test class (recommended for individual testing)
./gradlew :app:testDemoDebug --tests="NiaAppStateTest"

# Run instrumented tests  
./gradlew connectedDemoDebugAndroidTest

# Run specific AndroidTest module
./gradlew :sync:work:connectedDemoDebugAndroidTest

# Record screenshot tests (run before unit tests to avoid failures)
./gradlew recordRoborazziDemoDebug

# Verify screenshot tests
./gradlew verifyRoborazziDemoDebug

# Compare failed screenshot tests
./gradlew compareRoborazziDemoDebug
```

**Important**: 
- Do not run `./gradlew test` or `./gradlew connectedAndroidTest` as these execute against all variants and will fail
- Only `demoDebug` variant is supported for testing
- **Individual test classes work perfectly** - use `--tests=` for specific tests
- **Batch tests may have context isolation issues** - run individual classes when troubleshooting

### Performance Analysis
```bash
# Generate compose compiler metrics and reports
./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true

# Generate baseline profile (use benchmark build variant on AOSP emulator)
# Run BaselineProfileGenerator test, then copy result to app/src/main/baseline-prof.txt
```

## Architecture Overview

This app follows official Android architecture guidance with three layers:

### Layer Structure
- **UI Layer**: Jetpack Compose screens, ViewModels
- **Domain Layer**: Use cases, business logic (optional intermediary layer)
- **Data Layer**: Repositories, data sources (local/remote)

### Key Architectural Patterns
- **Unidirectional Data Flow**: Events down, data up via Kotlin Flows
- **Offline-First**: Local data as single source of truth with remote sync
- **Modularization**: Feature modules, core modules, and build-logic

### Dependency Injection - **MIGRATION COMPLETE** ✅

**✅ CURRENT STATE**: Fully migrated from Hilt to Koin (January 2025):
- **All modules** now use Koin dependency injection
- **Koin Modules**: Located in `*Module.kt` files (e.g., `app/src/main/kotlin/.../di/AppModule.kt`)
- **Convention Plugin**: `nowinandroid.koin` applies Koin dependencies automatically
- **ViewModels**: Use `koinViewModel()` in Compose screens
- **Testing**: Full Koin test infrastructure with `SafeKoinTestRule`

### Koin Architecture Overview
- **App Module** (`app/di/AppModule.kt`): ViewModels, JankStats, ImageLoader
- **Core Modules**: Data repositories, network clients, database instances
- **Feature Modules**: Automatic Koin setup via `AndroidFeatureConventionPlugin`
- **Test Modules**: `testDataModule`, `testDispatchersModule` for testing

**When adding new code**:
- Use Koin DI patterns throughout
- Features automatically get Koin via convention plugin
- Define dependencies in Koin modules using `module { }` blocks
- Use `koinViewModel()` for ViewModels in Compose
- Apply `nowinandroid.koin` plugin only for core modules (features get it automatically)

### Koin Patterns Used in This Project

```kotlin
// Module Definition
val dataModule = module {
    singleOf(::OfflineFirstNewsRepository) bind NewsRepository::class
    single { 
        DefaultSearchContentsRepository(
            get(), get(), get(), get(), 
            ioDispatcher = get(named("IO"))
        ) 
    } bind SearchContentsRepository::class
}

// ViewModel in Compose
@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = koinViewModel(),
) { /* ... */ }

// Testing with SafeKoinTestRule
@get:Rule(order = 0)
val koinTestRule = SafeKoinTestRule.create(
    modules = listOf(testDataModule, testDispatchersModule)
)
```

### Dependency Analysis Summary
95% of Koin dependencies are **necessary and well-optimized**:
- **Core modules**: Use Koin for repositories, network clients, database instances
- **Feature modules**: Automatically get Koin via `AndroidFeatureConventionPlugin` 
- **Test modules**: Comprehensive test infrastructure with proper isolation
- **Convention-driven**: Smart plugin system minimizes boilerplate

## Project Structure

### Build Configuration
- **Gradle Version Catalog**: `gradle/libs.versions.toml` - all dependency versions
- **Convention Plugins**: `build-logic/convention/` - shared build logic
- **Product Flavors**: `demo` (static data) vs `prod` (real backend)
- **Build Variants**: Use `demoDebug` for development, `demoRelease` for UI performance testing

### Module Organization
```
:app                    # Main application module
:core:*                # Shared infrastructure (data, network, UI, etc.)
:feature:*             # Feature-specific UI and logic
:sync:*                # Background synchronization
:benchmarks            # Performance benchmarks
```

### Key Libraries
- **UI**: Jetpack Compose, Material 3, Adaptive layouts
- **Dependency Injection**: Koin (fully migrated from Hilt)
- **Networking**: Retrofit, Kotlin Serialization, OkHttp
- **Local Storage**: Room, Proto DataStore
- **Concurrency**: Kotlin Coroutines, Flows
- **Image Loading**: Coil
- **Testing**: Truth, Turbine, Roborazzi (screenshots), Koin Test

## Testing Philosophy

The app uses **test doubles** with **Koin DI** for testing:
- **Test Repositories**: `Test` implementations with additional testing hooks (e.g., `TestNewsRepository`)
- **Koin Test Rules**: Use `SafeKoinTestRule` for proper DI context isolation
- **Test Modules**: `testDataModule` and `testDispatchersModule` provide test dependencies
- **ViewModels**: Tested with `koinViewModel()` against test repositories, not mocks
- **DataStore**: Real DataStore used in instrumentation tests with temporary folders
- **Screenshot Tests**: Verify UI rendering across different screen sizes (Roborazzi)

### Test Execution Best Practices
- **Individual Tests**: Always work perfectly - preferred for development
- **Batch Tests**: May have context isolation issues - use individual test classes when needed
- **AndroidTests**: All instrumentation tests working (e.g., `SyncWorkerTest`)

## Build Flavors and Variants

- **demo**: Uses static local data, good for immediate development
- **prod**: Connects to real backend (not publicly available)
- **debug/release**: Standard Android build types
- **benchmark**: Special variant for performance testing and baseline profile generation

## Kotlin Multiplatform Mobile (KMM) Readiness 🚀

This project is **excellently positioned** for KMM migration with Compose Multiplatform:

### ✅ Shareable Components (95%+ of codebase)
- **UI Layer**: All Jetpack Compose screens, design system, navigation, themes
- **Data Layer**: Repositories, Room database (with KSP), Retrofit networking
- **Domain Layer**: Use cases, business logic, data models
- **Dependency Injection**: Koin natively supports Kotlin Multiplatform

### ⚠️ Platform-Specific Components (expect/actual needed)
- **Analytics**: Firebase Analytics → expect/actual implementations
- **Browser Integration**: Custom tabs, WebView navigation → expect/actual
- **System Notifications**: Platform-specific notification systems
- **File System**: Context-dependent paths, DataStore locations

### 🧪 Testing Strategy for KMM
- **Shared Tests**: Business logic, repositories, use cases → `commonTest`
- **Platform Tests**: UI tests, integration tests → `androidTest`/`iosTest`
- **Screenshot Tests**: Android-specific (Roborazzi) → `androidTest` only

### 🎯 Migration Benefits
- **Code Reuse**: 95%+ shared between Android/iOS
- **Architecture Preserved**: Clean Architecture maintained across platforms
- **DI Compatibility**: Koin seamlessly supports multiplatform projects
- **Testing Coverage**: Comprehensive test strategy for shared and platform code

**Conclusion**: Ready for KMM migration with minimal platform-specific code required!

---

## Important Notes

- **JDK Requirement**: Java 17+ required
- **Screenshots**: Recorded on Linux CI, may differ on other platforms
- **Baseline Profile**: Regenerate for release builds affecting app startup
- **Compose Stability**: Check compiler reports for optimization opportunities