# Design System Architecture - Now in Android Patterns

## Overview

This document captures the architectural patterns, principles, and design decisions from the Now in Android `:core:designsystem` module. Use this as a reference when building a similar design system for your project.

---

## Core Principles

### 1. **Zero Internal Dependencies**
- The design system module is completely standalone
- No dependencies on other application modules
- Only depends on Compose and Android SDK libraries
- Makes it portable and reusable across projects

### 2. **Material 3 Foundation**
- Wraps Material 3 components rather than building from scratch
- Provides consistent defaults and app-specific customization
- Maintains compatibility with Material Design guidelines
- Easy to upgrade when Material 3 updates

### 3. **Encapsulation via Visibility**
- Color primitives are `internal` - only color schemes are public
- Theme data classes are `@Immutable` for Compose optimization
- Components expose clean, simplified APIs
- Implementation details hidden from consumers

### 4. **Composition Over Props**
- Uses `CompositionLocal` for theme propagation
- Avoids prop drilling for theme values
- Enables theme switching at any level
- Clean component APIs without theme parameters

---

## Architecture Layers

```
┌─────────────────────────────────────────────┐
│           Application Layer                  │
│    (Features, Screens, Business Logic)       │
└─────────────────────────────────────────────┘
                    ↓ uses
┌─────────────────────────────────────────────┐
│        Design System Components              │
│   (NiaButton, NiaTextField, etc.)            │
└─────────────────────────────────────────────┘
                    ↓ wraps
┌─────────────────────────────────────────────┐
│         Material 3 Components                │
│   (Button, TextField, etc.)                  │
└─────────────────────────────────────────────┘
                    ↓ uses
┌─────────────────────────────────────────────┐
│            Theme System                      │
│  Colors • Typography • Gradients • Tints     │
└─────────────────────────────────────────────┘
```

---

## Theme System Architecture

### Composition Local Pattern

**Purpose:** Propagate theme values down the component tree without explicit parameters.

**Implementation:**
```kotlin
// 1. Define data class with @Immutable
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

// 2. Create CompositionLocal
val LocalGradientColors = staticCompositionLocalOf { GradientColors() }

// 3. Provide in theme
CompositionLocalProvider(
    LocalGradientColors provides gradientColors,
) {
    MaterialTheme(content = content)
}

// 4. Consume in components
val gradientColors = LocalGradientColors.current
```

**Custom Composition Locals Used:**
- `LocalGradientColors` - App-specific gradient system
- `LocalBackgroundTheme` - Background colors and elevation
- `LocalTintTheme` - Icon tinting

### Color System Pattern

**Tonal Scale Approach:**
```
Color Naming: [ColorName][Lightness]
Examples: Purple10, Purple20, Purple40, Purple80, Purple90

10-30:  Dark mode accent colors
40:     Light mode primary colors
80-90:  Light mode accent colors
95-99:  Background/surface colors
```

**Semantic Mapping:**
```kotlin
lightColorScheme(
    primary = Purple40,           // Main brand color
    onPrimary = Color.White,      // Text/icons on primary
    primaryContainer = Purple90,  // Subtle primary background
    onPrimaryContainer = Purple10,// Text on primary container
    // ... complete Material 3 color scheme
)
```

**Benefits:**
- Easy to maintain (number = lightness percentage)
- Supports light/dark themes naturally
- Follows Material Design 3 conventions
- Clear semantic meaning

### Typography System Pattern

**Complete Type Scale:**
```
Display (3 sizes) - Large headlines, hero text
Headline (3 sizes) - Section headers
Title (3 sizes) - Subsections, card headers
Body (3 sizes) - Main content text
Label (3 sizes) - Buttons, tabs, chips
```

**Explicit Configuration:**
```kotlin
val NiaTypography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = Alignment.Center,
            trim = Trim.None,
        ),
        textDirection = TextDirection.Ltr,
        textAlign = TextAlign.Left,
    ),
    // ... all 15 text styles defined
)
```

**Key Features:**
- All properties explicitly set (no defaults)
- LineHeightStyle for precise text rendering
- Comments indicate usage (e.g., "Used for Button")
- Consistent LTR and Left alignment

### Theme Configuration Pattern

**Multi-Theme Support:**
```kotlin
@Composable
fun NiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,           // Brand variant
    disableDynamicTheming: Boolean = true,   // Material You
    content: @Composable () -> Unit,
)
```

**Theme Selection Logic:**
1. Check for brand variant (`androidTheme`)
2. Check for dynamic theming (Android 12+)
3. Fall back to default theme
4. Apply dark/light based on `darkTheme`

**Custom Theme Values:**
```kotlin
// Gradient system
val gradientColors = when {
    androidTheme -> DarkAndroidGradientColors
    supportsDynamicTheming() -> emptyGradientColors
    else -> defaultGradientColors
}

// Provide all custom theme values
CompositionLocalProvider(
    LocalGradientColors provides gradientColors,
    LocalBackgroundTheme provides backgroundTheme,
    LocalTintTheme provides tintTheme,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = NiaTypography,
        content = content,
    )
}
```

---

## Component Architecture Patterns

### 1. **Companion Object for Defaults**

**Pattern:**
```kotlin
object NiaButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f
    val OutlinedButtonBorderWidth = 1.dp
}
```

**Purpose:**
- Centralizes magic numbers
- Documents values not exposed by Material 3
- Easy to adjust without touching component logic
- Self-documenting code

**Usage:**
```kotlin
border = BorderStroke(
    width = NiaButtonDefaults.OutlinedButtonBorderWidth,
    color = MaterialTheme.colorScheme.outline,
)
```

### 2. **Overloaded Composables**

**Pattern: Provide both generic and structured variants**

```kotlin
// Generic content slot - maximum flexibility
@Composable
fun NiaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
)

// Structured variant - convenience
@Composable
fun NiaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
)
```

**Benefits:**
- Generic variant for custom layouts
- Structured variant for common use cases
- Structured variant delegates to generic
- Clear parameter names (text vs content)

### 3. **Internal Content Layouts**

**Pattern: Extract complex layouts to private functions**

```kotlin
@Composable
private fun NiaButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier.padding(
            start = if (leadingIcon != null) {
                ButtonDefaults.IconSpacing
            } else {
                0.dp
            },
        ),
    ) {
        text()
    }
}
```

**Benefits:**
- Keeps public API clean
- Reusable across overloaded variants
- Easy to test layout logic
- Clear separation of concerns

### 4. **Wrapper Pattern**

**Pattern: Wrap Material 3 with app-specific defaults**

```kotlin
@Composable
fun NiaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        contentPadding = contentPadding,
        content = content,
    )
}
```

**Benefits:**
- Single source of truth for component styling
- Easy to change app-wide defaults
- Maintains Material 3 compatibility
- Type-safe API

---

## Performance Patterns

### 1. **Immutable Data Classes**

```kotlin
@Immutable
data class GradientColors(...)
```

**Purpose:**
- Tells Compose runtime the value won't change
- Enables smart recomposition skipping
- Required for composition locals
- Better performance in large apps

### 2. **Packed Values**

```kotlin
@JvmInline
value class ScrollbarStateValue internal constructor(
    internal val packedValue: Long,
)

fun scrollbarStateValue(
    thumbSizePercent: Float,
    thumbMovedPercent: Float,
) = ScrollbarStateValue(
    packFloats(val1 = thumbSizePercent, val2 = thumbMovedPercent),
)
```

**Purpose:**
- Avoids boxing/unboxing of primitives
- Reduces memory allocations
- Better performance for frequently updated state
- Used in scrollbar for smooth scrolling

### 3. **Draw Caching**

```kotlin
Modifier.drawWithCache {
    // Expensive calculations here
    val gradient = Brush.linearGradient(...)

    onDrawBehind {
        drawRect(gradient)
    }
}
```

**Purpose:**
- Cache expensive calculations
- Only recalculate when size changes
- Used in gradient backgrounds
- Significant performance improvement

### 4. **Remember Updated State**

```kotlin
val currentTopColor by rememberUpdatedState(gradientColors.top)
```

**Purpose:**
- Captures latest value without restarting animations
- Used when animations reference external state
- Prevents unnecessary recompositions
- Important for smooth animations

---

## Preview System Pattern

### Multi-Preview Annotation

```kotlin
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews
```

**Usage:**
```kotlin
@ThemePreviews
@Composable
fun NiaButtonPreview() {
    NiaTheme {
        NiaBackground {
            NiaButton(onClick = {}, text = { Text("Test") })
        }
    }
}
```

**Benefits:**
- DRY principle for previews
- Ensures all components tested in both themes
- Single annotation to maintain
- Consistent preview experience

### Preview Best Practices

1. **Always wrap in theme:**
```kotlin
NiaTheme {
    NiaBackground {
        Component()
    }
}
```

2. **Use explicit sizes:**
```kotlin
NiaBackground(modifier = Modifier.size(150.dp, 50.dp))
```

3. **Show all variants:**
```kotlin
@ThemePreviews
@Composable
fun ButtonPreview() { /* filled */ }

@ThemePreviews
@Composable
fun OutlinedButtonPreview() { /* outlined */ }
```

---

## File Organization Pattern

```
core/designsystem/
├── build.gradle.kts
├── README.md
└── src/
    └── main/
        ├── kotlin/com/[company]/[app]/core/designsystem/
        │   ├── component/
        │   │   ├── Background.kt        (Also contains @ThemePreviews)
        │   │   ├── Button.kt
        │   │   ├── Chip.kt
        │   │   ├── IconButton.kt
        │   │   ├── Navigation.kt
        │   │   ├── Tag.kt
        │   │   ├── Tabs.kt
        │   │   ├── TopAppBar.kt
        │   │   ├── ViewToggle.kt
        │   │   ├── LoadingWheel.kt
        │   │   ├── DynamicAsyncImage.kt
        │   │   └── scrollbar/
        │   │       ├── Scrollbar.kt
        │   │       ├── ScrollbarExt.kt
        │   │       ├── LazyScrollbarUtilities.kt
        │   │       ├── AppScrollbars.kt
        │   │       └── ThumbExt.kt
        │   ├── icon/
        │   │   └── NiaIcons.kt
        │   └── theme/
        │       ├── Background.kt
        │       ├── Color.kt
        │       ├── Gradient.kt
        │       ├── Theme.kt
        │       ├── Tint.kt
        │       └── Type.kt
        └── res/
            └── drawable/
                └── ic_placeholder_default.xml
```

**Organization Rules:**
1. One component per file (except related overloads)
2. Theme system separate from components
3. Icons in dedicated package
4. Complex components get subdirectories (scrollbar)
5. Previews in same file as component

---

## Naming Conventions

### Components
- **Prefix:** All components start with app abbreviation (e.g., `Nia`)
- **Descriptive:** `NiaButton`, `NiaTopAppBar`, `NiaFilterChip`
- **Not:** Generic names like `CustomButton`, `MyButton`

### Colors
- **Pattern:** `[ColorName][Lightness]`
- **Examples:** `Purple40`, `DarkGreenGray95`
- **Semantic:** Use in color schemes, not directly in components

### Composition Locals
- **Pattern:** `Local[ThemeConcept]`
- **Examples:** `LocalGradientColors`, `LocalBackgroundTheme`
- **Not:** `GradientColorsLocal`, `ThemedBackground`

### Defaults Objects
- **Pattern:** `[Component]Defaults`
- **Examples:** `NiaButtonDefaults`, `NiaChipDefaults`
- **Contents:** Constants and Dp values

### Preview Functions
- **Pattern:** `[Component][Variant]Preview`
- **Examples:** `NiaButtonPreview`, `NiaOutlinedButtonPreview`
- **Always:** Annotated with `@ThemePreviews`

---

## Testing Strategy

### Screenshot Testing
- Uses Roborazzi for screenshot tests
- Captures all preview functions
- Ensures visual consistency across changes
- Part of CI/CD pipeline

### Preview Coverage
- Every component must have previews
- Both light and dark themes
- All major variants (filled, outlined, etc.)
- Different states (enabled, disabled, selected)

### Lint Rules
- Custom lint checks for design system usage
- Published from `:lint` module
- Enforces correct component usage
- Catches common mistakes

---

## Migration & Adoption Strategy

### Phase 1: Foundation (Week 1)
1. Set up module structure
2. Implement color system
3. Implement typography
4. Create base theme

### Phase 2: Core Components (Week 2-3)
1. Button variants
2. Text fields
3. Icons system
4. Basic layouts (Background)

### Phase 3: Navigation (Week 4)
1. Navigation bar/rail
2. Top app bar
3. Adaptive navigation

### Phase 4: Specialty (Week 5+)
1. Chips and tags
2. Loading indicators
3. Custom components (scrollbar)
4. Advanced animations

### Adoption Rules
1. **New code:** Must use design system components
2. **Refactoring:** Gradually migrate existing code
3. **No direct Material 3:** Always use design system wrappers
4. **Theme access:** Only via composition locals

---

## Common Pitfalls to Avoid

### ❌ Don't:
- Use color primitives directly in components
- Create components without defaults objects
- Skip preview annotations
- Mix Material 3 and design system components
- Put business logic in design system
- Create dependencies on other app modules

### ✅ Do:
- Use semantic color scheme values
- Centralize magic numbers in defaults
- Preview all components in both themes
- Use design system exclusively in UI
- Keep components purely presentational
- Maintain zero internal dependencies

---

## Summary

The Now in Android design system demonstrates:

1. **Clean Architecture** - Layered, encapsulated, standalone
2. **Performance** - Optimized with Compose best practices
3. **Developer Experience** - Consistent patterns, great previews
4. **Maintainability** - Easy to update, extend, and customize
5. **Type Safety** - Compile-time checks, no string-based theming

Use these patterns as a foundation for your own design system.
