# Design System Component Implementation Patterns

## Overview

This guide provides detailed patterns and examples for implementing components in your design system. Follow these patterns to maintain consistency and best practices.

---

## Component Template Structure

Every component file should follow this structure:

```kotlin
/*
 * Copyright [Year] [Your Company]
 * License header...
 */

package com.yourcompany.yourapp.core.designsystem.component

// 1. Imports (organized)
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// ...

// 2. Component Composables (public API first)
@Composable
fun AppComponent(...) { }

// 3. Helper Composables (private)
@Composable
private fun ComponentHelper(...) { }

// 4. Preview Composables
@ThemePreviews
@Composable
fun ComponentPreview() { }

// 5. Defaults Object (if needed)
object AppComponentDefaults { }
```

---

## Pattern 1: Basic Button Component

### Implementation

```kotlin
package com.yourcompany.yourapp.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yourcompany.yourapp.core.designsystem.theme.AppTheme

/**
 * Application filled button with generic content slot.
 *
 * Wraps Material 3 [Button] with app-specific defaults.
 *
 * @param onClick Called when button is clicked
 * @param modifier Modifier to apply to button
 * @param enabled Controls enabled state
 * @param contentPadding Internal padding
 * @param content Button content (text, icons, etc.)
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
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

/**
 * Application filled button with text content.
 *
 * Convenience variant for common use case.
 *
 * @param onClick Called when button is clicked
 * @param modifier Modifier to apply to button
 * @param enabled Controls enabled state
 * @param text Button text label
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit,
) {
    AppButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        text()
    }
}

@ThemePreviews
@Composable
fun AppButtonPreview() {
    AppTheme {
        AppBackground {
            AppButton(
                onClick = {},
                text = { Text("Click me") },
            )
        }
    }
}
```

### Key Points

1. **Generic variant first** - Most flexible API
2. **Convenience variant delegates** - Reuses generic implementation
3. **Material 3 wrapper** - Applies custom defaults
4. **KDoc comments** - Document parameters clearly
5. **Preview** - Shows component in app theme

---

## Pattern 2: Button with Icon Support

### Implementation

```kotlin
/**
 * Application button with text and optional leading icon.
 *
 * @param onClick Called when button is clicked
 * @param text Button text label
 * @param modifier Modifier to apply to button
 * @param enabled Controls enabled state
 * @param leadingIcon Optional icon before text
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    AppButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        } else {
            ButtonDefaults.ContentPadding
        },
    ) {
        AppButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

/**
 * Internal button content layout.
 *
 * Arranges icon and text with proper spacing.
 */
@Composable
private fun AppButtonContent(
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

@ThemePreviews
@Composable
fun AppButtonWithIconPreview() {
    AppTheme {
        AppBackground {
            AppButton(
                onClick = {},
                text = { Text("With Icon") },
                leadingIcon = {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}
```

### Key Points

1. **Nullable icon slot** - Optional feature
2. **Automatic padding** - Adjusts based on icon presence
3. **Private helper** - Encapsulates layout logic
4. **Separate preview** - Shows icon variant

---

## Pattern 3: Component with Defaults Object

### Implementation

```kotlin
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.unit.dp

/**
 * Application outlined button.
 *
 * @param onClick Called when button is clicked
 * @param modifier Modifier to apply to button
 * @param enabled Controls enabled state
 * @param content Button content
 */
@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(
            width = AppButtonDefaults.OutlinedButtonBorderWidth,
            color = if (enabled) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = AppButtonDefaults.DisabledOutlinedButtonBorderAlpha,
                )
            },
        ),
        content = content,
    )
}

/**
 * Application button default values.
 */
object AppButtonDefaults {
    /**
     * Default border width for outlined buttons.
     *
     * Note: Material 3 doesn't expose this value via ButtonDefaults.
     */
    val OutlinedButtonBorderWidth = 1.dp

    /**
     * Alpha for disabled outlined button borders.
     *
     * Note: Material 3 doesn't expose this value via ButtonDefaults.
     */
    const val DisabledOutlinedButtonBorderAlpha = 0.12f
}
```

### Key Points

1. **Defaults object** - Centralizes magic numbers
2. **KDoc on constants** - Explains why they exist
3. **Note about Material 3** - Documents API limitations
4. **Semantic names** - Clear purpose

---

## Pattern 4: Toggle Components

### Implementation

```kotlin
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.IconButtonDefaults

/**
 * Application icon toggle button.
 *
 * @param checked Whether button is checked
 * @param onCheckedChange Called when check state changes
 * @param modifier Modifier to apply to button
 * @param enabled Controls enabled state
 * @param icon Content when unchecked
 * @param checkedIcon Content when checked (defaults to [icon])
 */
@Composable
fun AppIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedIcon: @Composable () -> Unit = icon,
) {
    FilledIconToggleButton(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = if (checked) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = AppIconButtonDefaults.DisabledIconButtonContainerAlpha,
                )
            } else {
                Color.Transparent
            },
        ),
    ) {
        if (checked) checkedIcon() else icon()
    }
}

@ThemePreviews
@Composable
fun AppIconToggleButtonPreview() {
    AppTheme {
        AppBackground {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Unchecked
                AppIconToggleButton(
                    checked = false,
                    onCheckedChange = {},
                    icon = {
                        Icon(AppIcons.BookmarkBorder, contentDescription = null)
                    },
                    checkedIcon = {
                        Icon(AppIcons.Bookmark, contentDescription = null)
                    },
                )
                // Checked
                AppIconToggleButton(
                    checked = true,
                    onCheckedChange = {},
                    icon = {
                        Icon(AppIcons.BookmarkBorder, contentDescription = null)
                    },
                    checkedIcon = {
                        Icon(AppIcons.Bookmark, contentDescription = null)
                    },
                )
            }
        }
    }
}

object AppIconButtonDefaults {
    const val DisabledIconButtonContainerAlpha = 0.12f
}
```

### Key Points

1. **Boolean state** - checked/unchecked
2. **Callback pattern** - onCheckedChange
3. **Default checked icon** - Reuses unchecked icon if not specified
4. **Conditional rendering** - Switch icons based on state
5. **Multiple previews** - Show both states

---

## Pattern 5: Navigation Components

### Implementation

```kotlin
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.unit.dp

/**
 * Application navigation bar.
 *
 * @param modifier Modifier to apply
 * @param content Navigation items
 */
@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = AppNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}

/**
 * Application navigation bar item.
 *
 * @param selected Whether this item is selected
 * @param onClick Called when item is clicked
 * @param icon Item icon content
 * @param modifier Modifier to apply
 * @param selectedIcon Icon when selected (defaults to [icon])
 * @param enabled Controls enabled state
 * @param label Optional text label
 * @param alwaysShowLabel Whether to always show label
 */
@Composable
fun RowScope.AppNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = AppNavigationDefaults.navigationContentColor(),
            selectedTextColor = AppNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = AppNavigationDefaults.navigationContentColor(),
            indicatorColor = AppNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

/**
 * Application navigation default values.
 */
object AppNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}

@ThemePreviews
@Composable
fun AppNavigationBarPreview() {
    val items = listOf("Home", "Search", "Profile")
    val icons = listOf(
        AppIcons.Home,
        AppIcons.Search,
        AppIcons.Person,
    )

    AppTheme {
        AppNavigationBar {
            items.forEachIndexed { index, item ->
                AppNavigationBarItem(
                    selected = index == 0,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                )
            }
        }
    }
}
```

### Key Points

1. **RowScope extension** - Proper scope for navigation items
2. **Composable color functions** - Theme-aware defaults
3. **Icon switching** - Different icons for selected state
4. **Preview with data** - Shows real usage pattern

---

## Pattern 6: Filter Chips

### Implementation

```kotlin
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.ui.unit.dp

/**
 * Application filter chip.
 *
 * @param selected Whether chip is selected
 * @param onSelectedChange Called when selection changes
 * @param modifier Modifier to apply
 * @param enabled Controls enabled state
 * @param label Text label content
 */
@Composable
fun AppFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                label()
            }
        },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = AppIcons.Check,
                    contentDescription = null,
                )
            }
        } else {
            null
        },
        shape = CircleShape,
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.onBackground,
            selectedBorderColor = MaterialTheme.colorScheme.onBackground,
            disabledBorderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = AppChipDefaults.DisabledChipContentAlpha,
            ),
            disabledSelectedBorderColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = AppChipDefaults.DisabledChipContentAlpha,
            ),
            selectedBorderWidth = AppChipDefaults.ChipBorderWidth,
        ),
        colors = FilterChipDefaults.filterChipColors(
            labelColor = MaterialTheme.colorScheme.onBackground,
            iconColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = if (selected) {
                MaterialTheme.colorScheme.onBackground.copy(
                    alpha = AppChipDefaults.DisabledChipContainerAlpha,
                )
            } else {
                Color.Transparent
            },
            disabledLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = AppChipDefaults.DisabledChipContentAlpha,
            ),
            disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = AppChipDefaults.DisabledChipContentAlpha,
            ),
        ),
    )
}

@ThemePreviews
@Composable
fun AppFilterChipPreview() {
    AppTheme {
        AppBackground {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppFilterChip(
                    selected = false,
                    onSelectedChange = {},
                    label = { Text("Unselected") },
                )
                AppFilterChip(
                    selected = true,
                    onSelectedChange = {},
                    label = { Text("Selected") },
                )
            }
        }
    }
}

object AppChipDefaults {
    const val DisabledChipContainerAlpha = 0.12f
    const val DisabledChipContentAlpha = 0.38f
    val ChipBorderWidth = 1.dp
}
```

### Key Points

1. **ProvideTextStyle** - Sets typography for label
2. **Conditional leading icon** - Check icon when selected
3. **Extensive color config** - All states covered
4. **Multiple alpha values** - Different for container vs content

---

## Pattern 7: Loading Indicators

### Implementation

```kotlin
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Application loading indicator.
 *
 * Animated circular loading wheel.
 *
 * @param contentDesc Content description for accessibility
 * @param modifier Modifier to apply
 */
@Composable
fun AppLoadingWheel(
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            ),
        ),
        label = "rotation",
    )

    val baseColor = MaterialTheme.colorScheme.onBackground
    val accentColor = MaterialTheme.colorScheme.primary

    val colorAnim by infiniteTransition.animateColor(
        initialValue = baseColor,
        targetValue = accentColor,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "color",
    )

    Canvas(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer { rotationZ = rotationAnim }
            .semantics { contentDescription = contentDesc }
            .testTag("loadingWheel"),
    ) {
        drawCircle(
            color = colorAnim,
            radius = size.minDimension / 4,
        )
    }
}

@ThemePreviews
@Composable
fun AppLoadingWheelPreview() {
    AppTheme {
        AppBackground {
            AppLoadingWheel(contentDesc = "Loading")
        }
    }
}
```

### Key Points

1. **Infinite animation** - Uses `rememberInfiniteTransition`
2. **Multiple animations** - Rotation and color
3. **Accessibility** - Semantic content description
4. **Test tag** - For UI testing
5. **Theme colors** - Adapts to theme

---

## Pattern 8: Custom Background Components

### Implementation

```kotlin
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.yourcompany.yourapp.core.designsystem.theme.LocalGradientColors
import kotlin.math.tan

/**
 * Gradient background for decorative screens.
 *
 * @param modifier Modifier to apply
 * @param gradientColors Gradient colors to use
 * @param content Background content
 */
@Composable
fun AppGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    content: @Composable () -> Unit,
) {
    val currentTopColor by rememberUpdatedState(gradientColors.top)
    val currentBottomColor by rememberUpdatedState(gradientColors.bottom)

    Surface(
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            gradientColors.container
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    // Calculate gradient angle (11.06 degrees)
                    val offset = size.height * tan(
                        Math.toRadians(11.06).toFloat(),
                    )

                    val start = Offset(size.width / 2 + offset / 2, 0f)
                    val end = Offset(size.width / 2 - offset / 2, size.height)

                    val topGradient = Brush.linearGradient(
                        0f to if (currentTopColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentTopColor
                        },
                        0.724f to Color.Transparent,
                        start = start,
                        end = end,
                    )

                    val bottomGradient = Brush.linearGradient(
                        0.2552f to Color.Transparent,
                        1f to if (currentBottomColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentBottomColor
                        },
                        start = start,
                        end = end,
                    )

                    onDrawBehind {
                        drawRect(topGradient)
                        drawRect(bottomGradient)
                    }
                },
        ) {
            content()
        }
    }
}
```

### Key Points

1. **drawWithCache** - Performance optimization
2. **rememberUpdatedState** - Smooth color transitions
3. **Angled gradient** - Calculated using trigonometry
4. **Two-layer gradient** - Top and bottom fade
5. **Composition local** - Theme-aware gradients

---

## Component Checklist

When implementing a component, ensure:

### API Design
- [ ] Clear, semantic parameter names
- [ ] Modifier parameter (default = Modifier)
- [ ] Enabled parameter if interactive
- [ ] Generic and convenience variants (if applicable)

### Documentation
- [ ] KDoc comment on component
- [ ] KDoc on all parameters
- [ ] Usage example in comment (optional)
- [ ] Note about Material 3 wrapper

### Implementation
- [ ] Wraps Material 3 component (if available)
- [ ] Uses theme colors (not hardcoded)
- [ ] Private helper composables if needed
- [ ] Defaults object for magic numbers

### Preview
- [ ] `@ThemePreviews` annotation
- [ ] Shows component in AppTheme
- [ ] Shows in AppBackground
- [ ] Multiple variants (enabled, disabled, etc.)

### Accessibility
- [ ] Content descriptions where needed
- [ ] Semantic properties set
- [ ] Test tags for UI tests
- [ ] Works with screen readers

### Performance
- [ ] `@Immutable` on data classes
- [ ] `remember` for expensive calculations
- [ ] Stable parameters where possible
- [ ] No unnecessary recomposition

---

## Testing Components

### Preview Testing

```kotlin
@ThemePreviews
@Composable
fun ComponentPreview() {
    AppTheme {
        AppBackground {
            AppComponent()
        }
    }
}
```

### Screenshot Testing (Roborazzi)

```kotlin
@Test
fun appButton_lightTheme() {
    composeTestRule.setContent {
        AppTheme(darkTheme = false) {
            AppButton(
                onClick = {},
                text = { Text("Button") },
            )
        }
    }
    composeTestRule.onRoot()
        .captureRoboImage("button_light.png")
}
```

### Interaction Testing

```kotlin
@Test
fun appButton_clickable() {
    var clicked = false
    composeTestRule.setContent {
        AppButton(
            onClick = { clicked = true },
            text = { Text("Click me") },
        )
    }

    composeTestRule
        .onNodeWithText("Click me")
        .performClick()

    assertThat(clicked).isTrue()
}
```

---

## Advanced Patterns

### Pattern: State Hoisting

```kotlin
// ❌ Bad: Component manages its own state
@Composable
fun AppSearchBar() {
    var query by remember { mutableStateOf("") }
    TextField(value = query, onValueChange = { query = it })
}

// ✅ Good: State hoisted to caller
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    TextField(value = query, onValueChange = onQueryChange)
}
```

### Pattern: Slot APIs

```kotlin
// Flexible composition via slots
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footer: (@Composable () -> Unit)? = null,
) {
    Card(modifier = modifier) {
        Column {
            header()
            content()
            footer?.invoke()
        }
    }
}
```

### Pattern: Adaptive Components

```kotlin
@Composable
fun AppNavigationSuite(
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)

    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = modifier,
    ) {
        content()
    }
}
```

---

## Common Mistakes to Avoid

### ❌ Don't: Hardcode Colors
```kotlin
// Bad
Button(
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF6200EE),
    ),
)

// Good
Button(
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
    ),
)
```

### ❌ Don't: Skip Modifier Parameter
```kotlin
// Bad
@Composable
fun AppButton(onClick: () -> Unit) { }

// Good
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) { }
```

### ❌ Don't: Use Generic Names
```kotlin
// Bad
@Composable
fun CustomButton() { }

// Good
@Composable
fun AppButton() { }
```

### ❌ Don't: Manage State in Design System
```kotlin
// Bad - Component manages navigation
@Composable
fun AppNavigationBar(navController: NavController) { }

// Good - Component receives callbacks
@Composable
fun AppNavigationBar(
    selectedItem: Int,
    onItemClick: (Int) -> Unit,
) { }
```

---

## Summary

Follow these patterns for all components:

1. **Wrap Material 3** - Don't reinvent the wheel
2. **Use Composition Locals** - For theme values
3. **Provide Overloads** - Generic + convenience
4. **Centralize Constants** - In Defaults objects
5. **Add Previews** - With @ThemePreviews
6. **Document Everything** - Clear KDoc comments
7. **Hoist State** - Keep components pure
8. **Test Thoroughly** - Screenshots + interactions

This ensures a consistent, maintainable design system.
