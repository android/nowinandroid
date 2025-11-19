# Design System Module Setup Guide

## Overview

This guide provides step-by-step instructions for setting up a design system module following the Now in Android patterns. Use this as a practical reference when creating your `:core:designsystem` module.

---

## Module Structure

### Directory Layout

```
core/designsystem/
├── build.gradle.kts              # Module build configuration
├── .gitignore                     # Ignore build outputs
├── README.md                      # Module documentation
└── src/
    ├── main/
    │   ├── AndroidManifest.xml   # (Generated, minimal)
    │   ├── kotlin/
    │   │   └── [com]/[company]/[app]/core/designsystem/
    │   │       ├── component/    # UI components
    │   │       ├── icon/         # Icon system
    │   │       └── theme/        # Theme system
    │   └── res/
    │       ├── drawable/         # Placeholder images, backgrounds
    │       └── values/           # (Usually empty, colors in Kotlin)
    └── test/
        └── kotlin/               # Screenshot and unit tests
```

---

## Step 1: Create Module

### 1.1 Add to `settings.gradle.kts`

```kotlin
include(":core:designsystem")
```

### 1.2 Create `core/designsystem/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.yourcompany.yourapp.core.designsystem"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Compose BOM for version management
    val composeBom = platform("androidx.compose:compose-bom:2024.11.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose - Foundation
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.foundation:foundation-layout")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-util")
    api("androidx.compose.runtime:runtime")

    // Compose - Material 3
    api("androidx.compose.material3:material3")
    api("androidx.compose.material3:material3-adaptive")
    api("androidx.compose.material3:material3-adaptive-navigation-suite")
    api("androidx.compose.material:material-icons-extended")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    testImplementation("org.robolectric:robolectric:4.11.1")

    // Screenshot Testing (Optional)
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.10.0")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.10.0")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:1.10.0")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

### 1.3 Create `.gitignore`

```gitignore
/build
```

---

## Step 2: Create Package Structure

### 2.1 Base Package

Create directory: `src/main/kotlin/com/yourcompany/yourapp/core/designsystem/`

### 2.2 Subpackages

```bash
# Theme system
mkdir -p src/main/kotlin/com/yourcompany/yourapp/core/designsystem/theme

# Components
mkdir -p src/main/kotlin/com/yourcompany/yourapp/core/designsystem/component

# Icons
mkdir -p src/main/kotlin/com/yourcompany/yourapp/core/designsystem/icon

# Resources
mkdir -p src/main/res/drawable
```

---

## Step 3: Implement Theme Foundation

### 3.1 Color System (`theme/Color.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * [YourApp] color palette.
 *
 * Naming convention: [ColorName][Lightness]
 * - 10-30: Dark mode accent colors
 * - 40: Light mode primary colors
 * - 80-90: Light mode accent colors
 * - 95-99: Background/surface colors
 */

// Primary Colors
internal val Primary10 = Color(0xFF001F28)
internal val Primary20 = Color(0xFF003544)
internal val Primary30 = Color(0xFF004D61)
internal val Primary40 = Color(0xFF006780)
internal val Primary80 = Color(0xFF5DD5FC)
internal val Primary90 = Color(0xFFB8EAFF)

// Secondary Colors
internal val Secondary10 = Color(0xFF380D00)
internal val Secondary20 = Color(0xFF5B1A00)
internal val Secondary30 = Color(0xFF812800)
internal val Secondary40 = Color(0xFFA23F16)
internal val Secondary80 = Color(0xFFFFB59B)
internal val Secondary90 = Color(0xFFFFDBCF)

// Tertiary Colors
internal val Tertiary10 = Color(0xFF36003C)
internal val Tertiary20 = Color(0xFF560A5D)
internal val Tertiary30 = Color(0xFF702776)
internal val Tertiary40 = Color(0xFF8B418F)
internal val Tertiary80 = Color(0xFFFFA9FE)
internal val Tertiary90 = Color(0xFFFFD6FA)

// Error Colors
internal val Error10 = Color(0xFF410002)
internal val Error20 = Color(0xFF690005)
internal val Error30 = Color(0xFF93000A)
internal val Error40 = Color(0xFFBA1A1A)
internal val Error80 = Color(0xFFFFB4AB)
internal val Error90 = Color(0xFFFFDAD6)

// Neutral Colors
internal val Neutral10 = Color(0xFF1A1C1A)
internal val Neutral20 = Color(0xFF2F312E)
internal val Neutral90 = Color(0xFFE2E3DE)
internal val Neutral95 = Color(0xFFF0F1EC)
internal val Neutral99 = Color(0xFFFBFDF7)

internal val NeutralVariant30 = Color(0xFF414941)
internal val NeutralVariant50 = Color(0xFF727971)
internal val NeutralVariant60 = Color(0xFF8B938A)
internal val NeutralVariant80 = Color(0xFFC1C9BF)
internal val NeutralVariant90 = Color(0xFFDDE5DB)
```

### 3.2 Typography (`theme/Type.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.text.style.LineHeightStyle.Trim
import androidx.compose.ui.unit.sp

/**
 * [YourApp] typography scale.
 *
 * Based on Material Design 3 type scale.
 */
internal val AppTypography = Typography(
    // Display - Large headlines
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Headline - Section headers
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // Title - Card headers, dialogs
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Body - Main content
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),

    // Label - Buttons, tabs, chips
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
```

### 3.3 Gradient System (`theme/Gradient.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Gradient colors for backgrounds and decorative elements.
 */
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

/**
 * Composition local for [GradientColors].
 */
val LocalGradientColors = staticCompositionLocalOf { GradientColors() }
```

### 3.4 Background Theme (`theme/Background.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Background theme configuration.
 */
@Immutable
data class BackgroundTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
)

/**
 * Composition local for [BackgroundTheme].
 */
val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }
```

### 3.5 Tint System (`theme/Tint.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Icon tint theme configuration.
 */
@Immutable
data class TintTheme(
    val iconTint: Color = Color.Unspecified,
)

/**
 * Composition local for [TintTheme].
 */
val LocalTintTheme = staticCompositionLocalOf { TintTheme() }
```

### 3.6 Main Theme (`theme/Theme.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Light color scheme
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,
    primaryContainer = Primary90,
    onPrimaryContainer = Primary10,
    secondary = Secondary40,
    onSecondary = Color.White,
    secondaryContainer = Secondary90,
    onSecondaryContainer = Secondary10,
    tertiary = Tertiary40,
    onTertiary = Color.White,
    tertiaryContainer = Tertiary90,
    onTertiaryContainer = Tertiary10,
    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error10,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,
    surfaceVariant = NeutralVariant90,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50,
)

/**
 * Dark color scheme
 */
private val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = Primary20,
    primaryContainer = Primary30,
    onPrimaryContainer = Primary90,
    secondary = Secondary80,
    onSecondary = Secondary20,
    secondaryContainer = Secondary30,
    onSecondaryContainer = Secondary90,
    tertiary = Tertiary80,
    onTertiary = Tertiary20,
    tertiaryContainer = Tertiary30,
    onTertiaryContainer = Tertiary90,
    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = NeutralVariant80,
    outline = NeutralVariant60,
)

/**
 * Main application theme.
 *
 * @param darkTheme Whether to use dark theme (follows system by default)
 * @param disableDynamicTheming Whether to disable Material You dynamic colors
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    val gradientColors = when {
        !disableDynamicTheming && supportsDynamicTheming() -> {
            GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
        }
        else -> {
            GradientColors(
                top = colorScheme.inverseOnSurface,
                bottom = colorScheme.primaryContainer,
                container = colorScheme.surface,
            )
        }
    }

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    val tintTheme = when {
        !disableDynamicTheming && supportsDynamicTheming() -> {
            TintTheme(iconTint = colorScheme.primary)
        }
        else -> TintTheme()
    }

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
```

---

## Step 4: Create Icon System

### 4.1 Icons Object (`icon/Icons.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Application icons.
 *
 * Uses Material Icons. Custom icons should be added as drawable resources
 * and referenced here.
 */
object AppIcons {
    val Add = Icons.Rounded.Add
    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack
    val Bookmark = Icons.Rounded.Bookmark
    val BookmarkBorder = Icons.Rounded.BookmarkBorder
    val Bookmarks = Icons.Rounded.Bookmark
    val BookmarksBorder = Icons.Outlined.Bookmarks
    val Check = Icons.Rounded.Check
    val Close = Icons.Rounded.Close
    val MoreVert = Icons.Default.MoreVert
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
}
```

---

## Step 5: Create Preview System

### 5.1 Preview Annotation (`component/Background.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation for light and dark themes.
 */
@Preview(
    name = "Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class ThemePreviews
```

### 5.2 Background Components (`component/Background.kt`)

```kotlin
package com.yourcompany.yourapp.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yourcompany.yourapp.core.designsystem.theme.LocalBackgroundTheme

/**
 * Main application background.
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation

    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}
```

---

## Step 6: Usage in Application

### 6.1 Update App Module (`app/build.gradle.kts`)

```kotlin
dependencies {
    implementation(project(":core:designsystem"))
    // ... other dependencies
}
```

### 6.2 Apply Theme in MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppBackground {
                    // Your app content
                }
            }
        }
    }
}
```

---

## Step 7: Documentation

### 7.1 Create README.md

```markdown
# :core:designsystem

Design system module containing theme, components, and icons.

## Structure

- `theme/` - Colors, typography, theme configuration
- `component/` - Reusable UI components
- `icon/` - Icon system

## Usage

```kotlin
AppTheme {
    AppBackground {
        // Your content
    }
}
```

## Adding Components

1. Create component in `component/` package
2. Follow naming convention: `App[ComponentName]`
3. Add `@ThemePreviews` for preview
4. Create defaults object for constants

## Dependencies

This module has zero internal dependencies. Only depends on:
- Compose libraries
- Material 3
- Coil (image loading)
```

---

## Step 8: Verification Checklist

### Build & Compile
- [ ] Module builds successfully
- [ ] No compilation errors
- [ ] All dependencies resolved

### Theme System
- [ ] Colors defined with tonal scale
- [ ] Typography complete (15 styles)
- [ ] Theme composable works
- [ ] Composition locals accessible

### Preview System
- [ ] `@ThemePreviews` annotation created
- [ ] Light/dark previews render
- [ ] Preview in Android Studio works

### Integration
- [ ] App module can import design system
- [ ] Theme applies to app
- [ ] No circular dependencies

---

## Next Steps

1. **Add First Component:** Start with Button (see component-patterns.md)
2. **Customize Colors:** Replace placeholder colors with brand colors
3. **Add Custom Fonts:** If needed, add font resources
4. **Setup Screenshot Testing:** Configure Roborazzi
5. **Create Lint Rules:** Add custom lint checks

---

## Common Issues & Solutions

### Issue: "Unresolved reference: AppTheme"
**Solution:** Ensure correct package name in imports and build.gradle namespace

### Issue: Preview not showing
**Solution:**
1. Build project
2. Restart Android Studio
3. Invalidate caches

### Issue: Compose dependencies conflict
**Solution:** Use Compose BOM for version alignment

### Issue: Colors not applying
**Solution:** Ensure wrapping content in `AppTheme` composable

---

## Module Configuration Reference

### Minimum Android SDK Levels
- `minSdk = 21` (Android 5.0)
- `compileSdk = 35` (Latest)
- `targetSdk = 35` (Latest)

### Required Build Features
- `compose = true` (Compose UI)

### Required Kotlin Options
- `jvmTarget = "17"` (Java 17)

### API vs Implementation
- **API:** Compose libraries (exposed to consumers)
- **Implementation:** Coil, internal utilities (hidden from consumers)

---

## Summary

You now have:
1. ✅ Design system module structure
2. ✅ Theme foundation (colors, typography, composition locals)
3. ✅ Icon system
4. ✅ Preview system
5. ✅ Background components
6. ✅ Build configuration

Next: Implement components following patterns in `design-system-component-patterns.md`
