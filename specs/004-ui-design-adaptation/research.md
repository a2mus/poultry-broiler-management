# Research: UI Design Adaptation

This document details the research findings, best practices, and architectural decisions for adapting the Poultry Broiler Management Android application to the "Agri-Integrity Pro" design system.

---

## 1. Theme Configuration (Colors & Typography)

### Color Palette Mapping
- **Decision**: Update `Color.kt` to include the specific color tokens defined in `agri_integrity_pro/DESIGN.md`, mapping them to `LightColorScheme` and `DarkColorScheme` in `Theme.kt`.
- **Rationale**: Keeps the codebase aligned with the design tokens.
- **Alternatives Considered**: Direct hex injection in composables (rejected because it violates Article 3 of the Project Constitution).

### Typography Integration
- **Decision**: Load "Outfit" and "Inter" fonts via the Compose `GoogleFonts` API or local font assets. Define typography styles in `Type.kt`.
- **Rationale**: Custom typography gives the app a modern, high-end feel.
- **Alternatives Considered**: Using default Material 3 fonts (rejected due to design spec alignment requirements).

---

## 2. Shapes & Spacing

### 8dp Baseline Grid & Spacing
- **Decision**: Declare a custom `Spacing` class containing standard dimensions:
  - `micro = 4.dp`
  - `small = 8.dp`
  - `medium = 16.dp`
  - `large = 24.dp`
- **Rationale**: Standardizes padding and margins, ensuring consistent visual rhythm.
- **Alternatives Considered**: Inline raw DP declarations (rejected because it causes styling inconsistencies).

### Shapes Mapping
- **Decision**: Configure `Shape.kt` with rounded shapes:
  - `small` (badges) = `8.dp`
  - `medium` (cards) = `16.dp`
  - `large` (buttons) = `24.dp` (pill-shaped)
  - `extraLarge` (dialogs) = `28.dp`
- **Rationale**: Direct compliance with Article 3.1 of the Project Constitution.
- **Alternatives Considered**: Default Material 3 shapes (rejected to keep styling accurate to the Stitch UI specification).

---

## 3. Screen Layout & Navigation Adaptation

### Home Bento Grid Layout
- **Decision**: Implement a staggered or responsive grid container in Jetpack Compose for the Home screen using standard `LazyVerticalGrid` or custom grid containers.
- **Rationale**: Bento layouts provide a clean, modern dashboard structure.
- **Alternatives Considered**: Simple vertical scroll list (rejected because it deviates from the Bento Grid specifications of `accueil_gestionnaire_avicole`).

### Dimensions Wizard & Live 2D Preview
- **Decision**: Implement the 2D preview canvas using Jetpack Compose `Canvas` drawing simple shapes with scaled bounds. The canvas updates dynamically on dimension inputs.
- **Rationale**: Live visual feedback helps users verify structural dimensions.
- **Alternatives Considered**: Static placeholder images (rejected; a dynamic canvas is required).

---

## 4. Localization & RTL Support

### RTL Mirroring
- **Decision**: Enforce RTL behavior by using Compose's native support for layout mirroring. Ensure all navigation icons and next/previous wizard buttons are correctly positioned based on layout direction.
- **Rationale**: Mandatory for Arabic translation support.
- **Alternatives Considered**: Manual custom offsets for Arabic (rejected; standard RTL localization is cleaner and more robust).
