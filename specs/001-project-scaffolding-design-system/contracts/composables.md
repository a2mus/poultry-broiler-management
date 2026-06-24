# Contract: Shared Composable APIs

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`
**Layer**: Presentation (Jetpack Compose)

> These composables are **themed shells** per spec clarification: they accept content parameters and apply design tokens, but have no interactive behavior or state callbacks. Full interactivity is deferred to consuming features.

---

## StatusBadge

Located at: `presentation/components/StatusBadge.kt`

```kotlin
/**
 * A themed badge component for displaying status labels.
 * Applies design token colors, shape, and typography.
 *
 * Themed shell: renders a styled badge with text content.
 * No click behavior or state management.
 *
 * @param text The status label to display
 * @param color The badge background color (defaults to MaterialTheme.colorScheme.primaryContainer)
 * @param modifier Optional modifier for layout customization
 */
@Composable
fun StatusBadge(
    text: String,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier,
)
```

**Design tokens applied**:
- Shape: 8dp corner radius (badge corners per FR-005)
- Typography: `MaterialTheme.typography.labelSmall` (Inter)
- Color: `color` parameter for background, `onPrimaryContainer` for text
- Padding: 4dp vertical, 8dp horizontal (8dp grid)

---

## NumericInputField

Located at: `presentation/components/NumericInputField.kt`

```kotlin
/**
 * A themed numeric input field with a trailing unit label.
 * Applies design token styling and enforces numeric keyboard type.
 *
 * Themed shell: renders a styled OutlinedTextField configured for numeric input
 * with a trailing unit label. No validation, no state callbacks.
 *
 * @param value The current text value to display
 * @param unitLabel The unit suffix displayed as trailing content (e.g., "m", "kg", "m²")
 * @param label The field label text
 * @param modifier Optional modifier for layout customization
 */
@Composable
fun NumericInputField(
    value: String,
    unitLabel: String,
    label: String,
    modifier: Modifier = Modifier,
)
```

**Design tokens applied**:
- Shape: Material 3 `OutlinedTextField` default shape
- Typography: `MaterialTheme.typography.bodyLarge` (Inter) for input, `bodyMedium` for unit label
- Color: `MaterialTheme.colorScheme.outline` for border, `onSurface` for text
- Touch target: minimum 48dp height (FR-019)
- Keyboard: `KeyboardType.Decimal` (numeric keypad)

---

## BottomSheet

Located at: `presentation/components/BottomSheet.kt`

```kotlin
/**
 * A themed modal bottom sheet container.
 * Applies design token styling (shape, elevation, colors).
 *
 * Themed shell: renders a ModalBottomSheet with styled container.
 * No dismiss callbacks or state management.
 *
 * @param title Optional title text displayed at the top of the sheet
 * @param modifier Optional modifier for layout customization
 * @param content The composable content to display inside the sheet
 */
@Composable
fun BottomSheet(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
)
```

**Design tokens applied**:
- Shape: 28dp top corner radius (dialog corners per FR-005)
- Elevation: 12dp (modal elevation per FR-006)
- Color: `MaterialTheme.colorScheme.surface` background, `onSurface` for title
- Typography: `MaterialTheme.typography.titleMedium` (Outfit) for title
- Spacing: 16dp horizontal padding, 24dp top padding (8dp grid)
- Drag handle: Material 3 default drag indicator

---

## Design Token Constants

All composables reference tokens from the theme system. No hardcoded values are permitted.

| Token | Value | Source |
|-------|-------|--------|
| Badge corners | 8dp | `Shape.kt` / FR-005 |
| Card corners | 16dp | `Shape.kt` / FR-005 |
| Button corners | 24dp | `Shape.kt` / FR-005 |
| Dialog/sheet corners | 28dp | `Shape.kt` / FR-005 |
| Flat elevation | 0dp | `Elevation.kt` / FR-006 |
| Card elevation | 2dp | `Elevation.kt` / FR-006 |
| Hover elevation | 6dp | `Elevation.kt` / FR-006 |
| Modal elevation | 12dp | `Elevation.kt` / FR-006 |
| Min touch target | 48dp | FR-019 / WCAG AA |
| Spacing unit | 4dp | `Spacing.kt` / FR-004 |
| Base spacing | 8dp | `Spacing.kt` / FR-004 |
