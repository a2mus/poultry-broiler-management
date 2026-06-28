---
name: Agri-Integrity Pro
colors:
  surface: '#f7f9ff'
  surface-dim: '#d2dbe6'
  surface-bright: '#f7f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#ecf4ff'
  surface-container: '#e6effa'
  surface-container-high: '#e0e9f4'
  surface-container-highest: '#dae3ee'
  on-surface: '#131d24'
  on-surface-variant: '#3e4946'
  inverse-surface: '#28313a'
  inverse-on-surface: '#e8f2fd'
  outline: '#6e7976'
  outline-variant: '#bdc9c5'
  surface-tint: '#006b5f'
  primary: '#00685c'
  on-primary: '#ffffff'
  primary-container: '#1e8275'
  on-primary-container: '#f2fffb'
  inverse-primary: '#7dd6c7'
  secondary: '#865300'
  on-secondary: '#ffffff'
  secondary-container: '#fcae49'
  on-secondary-container: '#6e4300'
  tertiary: '#006a35'
  on-tertiary: '#ffffff'
  tertiary-container: '#008645'
  on-tertiary-container: '#f4fff2'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#99f3e3'
  primary-fixed-dim: '#7dd6c7'
  on-primary-fixed: '#00201c'
  on-primary-fixed-variant: '#005047'
  secondary-fixed: '#ffddb9'
  secondary-fixed-dim: '#ffb963'
  on-secondary-fixed: '#2b1700'
  on-secondary-fixed-variant: '#663e00'
  tertiary-fixed: '#8af9a9'
  tertiary-fixed-dim: '#6edc8f'
  on-tertiary-fixed: '#00210c'
  on-tertiary-fixed-variant: '#005228'
  background: '#f7f9ff'
  on-background: '#131d24'
  surface-variant: '#dae3ee'
  light-background: '#F5F7F8'
  light-surface: '#EDEFF1'
  light-surface-variant: '#E1E4E7'
  dark-background: '#0C1013'
  dark-surface: '#171F26'
  dark-surface-variant: '#242F3A'
  success: '#229954'
  warning: '#D4AC0D'
  danger: '#CB4335'
  dark-primary: '#2ECC71'
  dark-secondary: '#F39C12'
  dark-success: '#27AE60'
  dark-warning: '#F1C40F'
  dark-danger: '#E74C3C'
typography:
  display-lg:
    fontFamily: Outfit
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  title-md:
    fontFamily: Outfit
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 26px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 22px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  numeric-data:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '500'
    lineHeight: 18px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  micro: 4px
  small: 8px
  medium: 16px
  large: 24px
  baseline: 8px
---

## Brand & Style

The design system is engineered for a premium, high-integrity professional tool dedicated to agricultural management. It prioritizes precision, reliability, and field-readiness. The visual language bridges the gap between sophisticated technical software and rugged utility, ensuring it feels like an essential piece of modern farming equipment rather than a generic consumer app.

The design style is **Corporate / Modern** with a focus on **Field Glare Reduction**. It utilizes a structured grid, clear information hierarchy, and high-contrast elements to ensure readability in outdoor environments. The aesthetic is clean and balanced, evoking an emotional response of confidence, professional competence, and industrial-grade stability.

Key attributes:
- **Professionalism:** High-quality typography and consistent spacing.
- **Integrity:** Solid, grounded shapes and a technical color palette.
- **Precision:** Specific numeric data styling and clear spatial layouts.

## Colors

The color system is optimized for two specific environmental contexts:

**Light Mode (Field Glare Reduction):**
Tailored for outdoor use where sunlight can wash out screens. It uses a high-contrast palette with "Forest Teal" as the primary anchor and "Soft Pearl" backgrounds to reduce eye strain.

**Dark Mode (Sleek Carbon):**
Designed for low-light indoor environments or office use. It utilizes "Vibrant Mint" for primary actions and "Deep Obsidian" surfaces to provide a sophisticated, high-tech appearance.

**Functional Color Logic:**
- **Success:** Emerald tones for EU compliance and completed tasks.
- **Warning:** Gold/Amber tones for density thresholds and pending items.
- **Danger:** Terracotta/Crimson tones for critical failures or high-priority risks.

## Typography

This design system employs a dual-font strategy to balance character and utility:

- **Outfit (Display & Headers):** Used for screen titles, card headers, and hero elements. Its geometric structure provides a modern, high-integrity feel that aligns with engineering and design work.
- **Inter (Body & Data):** Used for all functional text, descriptions, and labels. It is chosen for its extreme legibility at small scales and neutral, professional tone.

**Specialized Formatting:**
- **Numeric Data:** Uses Inter Medium with specific letter spacing to ensure dimensions and calculations are easily scannable in tabular formats.
- **Status Labels:** High-weight Inter at small sizes ensures that even brief badges are instantly readable.

## Layout & Spacing

The layout follows a strict **8dp baseline grid** to ensure vertical and horizontal rhythm across all screens.

**Grid Architecture:**
- **Phone:** 4-column fluid grid with 16px margins.
- **Tablet:** 8-column adaptive grid to handle data-heavy views and comparisons.

**Spacing Rhythm:**
- **4px (Micro):** Minimal separation between icons and adjacent text labels.
- **8px (Small):** Internal padding for small components like chips or list items.
- **16px (Medium):** Standard padding for cards, containers, and general element gutters.
- **24px (Large):** Global page margins and separation between major sections of the UI.

## Elevation & Depth

Visual hierarchy is established through a combination of **Tonal Layers** and **Ambient Shadows** that reflect Material 3 principles:

- **Surface Levels:** Surface and Surface Variant colors are used to create containment without relying solely on shadows. In dark mode, these layers use "Charcoal Carbon" and "Surface Variant" to differentiate between the background and interactive components.
- **Elevation Levels:**
  - **Level 0 (Flat):** Used for background surfaces.
  - **Level 1 (2dp):** Default card elevation, providing a subtle lift from the background.
  - **Level 2 (6dp):** Interactive states (hover/press) to provide tactile feedback.
  - **Level 3 (12dp):** Floating elements like FABs, Bottom Sheets, and Modals, casting a more pronounced but diffused shadow to indicate they are at the top of the stack.

## Shapes

The shape language combines softness with structural clarity:

- **Cards:** Use `16dp` rounded corners for a modern, approachable containment.
- **Buttons:** Use a **Pill-shaped** (`24dp`) design for primary and secondary actions, ensuring they are easily distinguishable from other rectangular UI elements.
- **Badges:** Use a tighter `8dp` radius for a compact, technical look.
- **Dialogs:** Employ a generous `28dp` radius to soften the focus when high-level decisions or wizards are presented.

## Components

### Buttons
- **Primary:** Pill-shaped (`24dp`), Forest Teal (Light) / Vibrant Mint (Dark).
- **Secondary:** Outlined or Warm Ochre (Light) / Sunset Gold (Dark) for specific high-visibility actions like GPS detection.
- **Accessibility:** All buttons maintain a minimum `48x48dp` touch target.

### Cards
- **Project Cards:** Feature `16dp` corners, 16px internal padding, and include a 2D thumbnail preview. Use Level 1 elevation.
- **Micro-interactions:** Cards should scale down to `98%` on long-press with a ripple effect.

### Input Fields
- **Numeric Fields:** Must feature a persistent trailing unit label (e.g., `m`, `CFM`) and trigger the numeric keypad by default.
- **Segmented Controls:** Used for categorical selections (e.g., Roof Type) with illustrative icons.

### Status & Compliance Badges
- **Draft:** Gray Background, On-Surface text.
- **Completed:** Primary color background, On-Primary text.
- **Welfare Pass/Fail:** High-contrast text on low-saturation Emerald (Pass) or Red (Fail) backgrounds.
- **High Priority:** Terracotta Red background with White text for critical alerts.

### Interactive 2D Canvas
- Blueprint elements use vectorized symbols.
- **Exhaust Fans:** Propeller icon in Dark Gray (Light) or Teal Mint (Dark).
- **Heaters:** Flame icon in Red (Light) or Orange (Dark).
- **Edit Mode:** Selected elements are highlighted with a `3498DB` selection ring and drag handles.

### Sliders
- **Capacity Slider:** Track color must dynamically transition from Green to Yellow to Red as the value approaches welfare density thresholds.