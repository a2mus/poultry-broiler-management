# Feature Specification: UI Design Adaptation

**Feature Branch**: `004-ui-design-adaptation`

**Created**: 2026-06-27

**Status**: Draft

**Input**: User description: "adapt the app to this ui design @[d:\Developpement\Projets\WEB\poultry-broiler-management\stitch_poultry_broiler_management.zip]"

## Clarifications

### Session 2026-06-27

- Q: Which currency should be used for the financial analysis screen? → A: DZD (Algerian Dinar) is the primary currency for local calculations, with support for locale-based currency formatting.
- Q: Do we support dynamic light/dark mode switching? → A: Yes, the app MUST support both light mode (Field Glare Reduction) and dark mode (Sleek Carbon) based on system settings or user override.
- Q: What font family is preferred for display titles? → A: Outfit is used for display titles and card headers, while Inter is used for body text and numeric data.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - App Theme and Styling Update (Priority: P1)

As a user, when I open the application, I want the overall appearance (colors, typography, shapes, and spacing) to reflect the new premium "Agri-Integrity Pro" design system so that it feels modern, professional, and readable in outdoor conditions.

**Why this priority**: The design system provides the visual foundations (colors, fonts, corners, spacing) that all screens depend on. It establishes the glare-reducing primary colors and typography necessary for field use.

**Independent Test**: Can be verified by changing the system theme between Light and Dark mode and opening any screen to check that the correct colors (Teal/Carbon), fonts (Outfit/Inter), and corner shapes (rounded/pill) are applied.

**Acceptance Scenarios**:

1. **Given** the app is in Light Mode, **When** a screen renders, **Then** the background is Soft Pearl (`#f7f9ff`), the primary elements are Forest Teal (`#00685c`), and the body text is Outfit or Inter.
2. **Given** the app is in Dark Mode, **When** a screen renders, **Then** the background is Deep Obsidian (`#0C1013`), surfaces are Charcoal Carbon (`#171F26`), and primary highlights use Vibrant Mint (`#2ECC71`).
3. **Given** any standard button, **When** it renders, **Then** it has a pill shape (rounded corner `24dp`) and minimum touch dimensions of `48dp` x `48dp`.

---

### User Story 2 - Bento Grid Home Screen (Priority: P1)

As a poultry manager, when I open the app, I want to see a clean, responsive Home dashboard with a Bento Grid layout showing active projects, their locations, compliance scores, and a quick-action card to add new installations.

**Why this priority**: The home dashboard is the entry point for all user workflows. It provides an immediate overview of active projects and key compliance scores.

**Independent Test**: Can be tested by opening the Home screen, verifying the layout structure matches the Bento Grid design, checking the active project cards list, and verifying the compliance score display.

**Acceptance Scenarios**:

1. **Given** the user is on the Home dashboard, **When** the page loads, **Then** active projects are displayed as cards with a top image, status badge (TERMINÉ, BROUILLON, EN ATTENTE), location metadata, and a large numeric compliance score (e.g. `98%`).
2. **Given** the Home dashboard layout, **When** viewed on a tablet, **Then** it adaptive-grids to an 8-column layout rather than the phone's single column list.
3. **Given** the new project placeholder card, **When** tapped, **Then** it triggers the project creation wizard dialog.

---

### User Story 3 - House Dimensions Step with Live Preview (Priority: P1)

As a consultant, when designing a broiler house, I want to enter building dimensions, select insulation/wall materials, and select roof types while seeing a live 2D outline preview that resizes dynamically based on my inputs.

**Why this priority**: The dimensions step collects structural measurements essential for calculating house capacity, ventilation needs, and financial projections.

**Independent Test**: Can be tested by entering values on the Dimensions wizard step and confirming that the total surface area updates and the 2D box canvas resizes dynamically.

**Acceptance Scenarios**:

1. **Given** the user is on the Dimensions step, **When** they enter length `120m` and width `20m`, **Then** the estimated total surface displays `2,400 m²` and the 2D canvas draws a proportional rectangle with `120m` and `20m` ruler labels.
2. **Given** the wall insulation options, **When** the user selects "Préfabriqué", **Then** the radio card is highlighted in Teal with a checkmark and the choice is persisted.
3. **Given** the roof type options, **When** the user selects "À Pignon", **Then** the selection card shows active checked status and ridge height parameters appear if required.

---

### User Story 4 - Bilingual Navigation & RTL Mirroring (Priority: P1)

As an Arabic-speaking farm agent, I want the entire application interface to mirror from right to left (RTL) and use translated Arabic texts when the device language is set to Arabic.

**Why this priority**: The app must support field agents in Algeria where Arabic is a primary language. Proper RTL layout and translation are critical for usability.

**Independent Test**: Verified by switching device locale to Arabic (`ar`) and opening the app, checking that the layout, navigation bars, icons, text labels, and wizard buttons are mirrored.

**Acceptance Scenarios**:

1. **Given** the device locale is Arabic, **When** the Home dashboard loads, **Then** the hamburger menu appears on the right, title "لوحة القيادة" appears, and bottom navigation bar items are ordered right-to-left.
2. **Given** the wizard navigation bar in Arabic, **When** rendering next/previous controls, **Then** the "Suivant/التالي" button is on the left pointing left, and "Précédent/السابق" is on the right.

---

### User Story 5 - Interactive 2D Blueprint Canvas (Priority: P2)

As a designer, I want to view a detailed 2D blueprint grid showing ventilation fans, heaters, feed lines, and cooling pads represented by color-coded vectorized symbols, and be able to zoom/pan the blueprint.

**Why this priority**: High-fidelity visualization allows users to verify equipment distribution and placement limits according to structural shapes.

**Independent Test**: Verified by opening the Blueprint tab, performing pinch-to-zoom/pan gestures, and confirming that the fans (propellers), heaters (flames), and lines draw correctly on the grid.

**Acceptance Scenarios**:

1. **Given** the blueprint canvas, **When** displaying exhaust fans, **Then** they render as propeller icons along the tunnel exhaust wall in Dark Gray (light mode) or Teal Mint (dark mode).
2. **Given** the blueprint canvas, **When** the user pinches the screen, **Then** the scale zoom level adjusts smoothly from `0.5x` up to `4.0x`.

---

### User Story 6 - Risk Assessment and compliance Checklist (Priority: P2)

As an auditor, I want to view a risk assessment tab containing a radial risk gauge, mortality risk factor breakdowns, environmental sensor metrics (temp, humidity, air quality), and an EU welfare compliance checklist.

**Why this priority**: Compliance verification is needed to validate stocking density limits and biosecurity measures.

**Independent Test**: Checked by navigating to the Risk Assessment tab, verifying that the gauge displays a score, the environmental cards pulse, and checkmarks on the EU checklist update compliance percentages.

**Acceptance Scenarios**:

1. **Given** the risk dashboard, **When** loading the risk score, **Then** a radial SVG gauge illustrates the current risk (e.g. `54/100`) and highlights high-risk areas.
2. **Given** the EU welfare checklist, **When** checkmarks are toggled, **Then** the compliance score percentage recalculates in real-time.

---

### User Story 7 - Financial Analysis and Enhancements (Priority: P2)

As a business owner, I want to review CapEx/OpEx summaries, see an ROI payback area chart, simulate flock cycles via a slider, and see recommended upgrades with current vs. proposed comparison boxes.

**Why this priority**: Feasibility planning relies on financial projections and return timelines.

**Independent Test**: Tested by dragging the flock cycles simulation slider and verifying that annual profits recalculate dynamically.

**Acceptance Scenarios**:

1. **Given** the Financial Analysis tab, **When** the flock cycles slider is adjusted, **Then** the Estimated Annual Profit text updates in real-time.
2. **Given** the Technical Improvements tab, **When** looking at recommendations, **Then** they display current vs. proposed comparisons (e.g. R-12 vs R-28 insulation) and show ROI payback timelines.

## Edge Cases

- **Device Rotation**: The 2D previews, canvases, input values, and selected tabs must survive configuration changes without losing entered state or resetting animations.
- **RTL Canvas Orientation**: Drawing coordinates and boundary labels on the 2D blueprint/preview canvas must remain mathematically consistent and visually aligned regardless of the text layout direction.
- **Extreme Input Thresholds**: Inputs such as a width of `500m` or capacity of `1,000,000` must be restricted by domain validators, displaying inline warnings instead of overflowing text fields or crashing calculations.
- **Theme/Language Switching at Runtime**: Changing light/dark mode or system language must update all colors, fonts, and texts immediately without requiring app restart.

## Requirements *(mandatory)*

### Functional Requirements

#### Design System & Foundations
- **FR-001**: System MUST implement the color tokens specified in `DESIGN.md` for light mode (glare-reducing Soft Pearl and Forest Teal) and dark mode (Carbon Obsidian and Mint).
- **FR-002**: System MUST apply the Outfit font family for display/headings and the Inter font family for body, labels, and numeric figures.
- **FR-003**: System MUST enforce an 8dp baseline grid for spacing, margins, and padding.
- **FR-004**: System MUST apply corner radius configurations: `16dp` for cards, `24dp` for pill buttons, `8dp` for badges, and `28dp` for dialogs.

#### Home Dashboard Screen
- **FR-005**: Home Screen MUST implement the Bento Grid layout with responsive card modules for active projects.
- **FR-006**: Project Cards MUST display the project name, location icon, status badge (French/Arabic translated), capacity, ventilation state, and circular/numerical compliance score.
- **FR-007**: System MUST provide a New Project dashed card with an add icon that triggers the creation wizard.
- **FR-008**: Home Screen MUST show a performance reports summary card in primary Teal coloring.

#### House Dimensions Wizard Step
- **FR-009**: The Dimensions wizard screen MUST show a progress badge ("Étape 1/4"), structural inputs (Length, Width, Height) with trailing units, and wall material/roof type radio cards.
- **FR-010**: System MUST render a scaled 2D blueprint box preview that resizes dynamically as width/length values change.
- **FR-011**: System MUST highlight selected roof types and insulation types with a checkmark and active colored container.
- **FR-012**: System MUST calculate the estimated total surface area and show optimal capacity suggestions.

#### Blueprint Dashboard Tab
- **FR-013**: Blueprint Tab MUST show a 2D grid background with vectorized symbols for exhaust fans, heaters, cooling pads, feed lines, and water lines in the themed design colors.
- **FR-014**: Blueprint canvas MUST support pinch-to-zoom (0.5x to 4.0x) and panning gestures.
- **FR-015**: Blueprint Tab MUST display a symbol legend panel indicating equipment types.

#### Risk Assessment Dashboard Tab
- **FR-016**: Risk Tab MUST feature a radial gauge reflecting the safety score, a mortality risk factor list, and pulsing status indicators for real-time sensor metrics (temperature, humidity, air quality).
- **FR-017**: Risk Tab MUST present the EU Welfare Checklist with interactive checkboxes updating the compliance score.

#### Financial & Enhancements Dashboard Tabs
- **FR-018**: Financial Tab MUST display CapEx/OpEx bento grid cards, an ROI area chart, and a flock-cycles slider that dynamically updates annual profit.
- **FR-019**: Enhancements Tab MUST list upgrades categorized by priority (Critical, High, Medium) with side-by-side comparison boxes showing Current vs. Proposed states.

#### Localization & Accessibility
- **FR-020**: System MUST mirror layouts from right to left (RTL) and use translated Arabic strings when device locale is Arabic.
- **FR-021**: All interactive components MUST have touch targets of at least `48dp` x `48dp`.
- **FR-022**: Text readability MUST meet WCAG AA contrast ratios (4.5:1) for standard text and WCAG AAA (7:1) for critical alerts.

### Key Entities

- **AppTheme**: The configuration representing colors, typography, shapes, and elevations.
- **ProjectOverview**: Metadata representing a project card's display data (compliance score, capacity, status, location).
- **InsulationSelect**: Option model containing wall insulation type (Prefab, Block, Steel) with description and icon identifier.
- **RoofTypeSelect**: Option model containing roof shape type (Gable, Flat) with icon identifier.
- **RiskAssessment**: Model containing current safety score, list of environmental parameters (temp, humidity, air quality), and checkbox compliance list.
- **FinancialSummary**: Data structure containing CapEx, OpEx, flock cycle counts, and ROI payback parameters.
- **UpgradeProposal**: Upgrade card containing priority, current state, proposed state, ROI duration, and estimated cost.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Application colors, typography, and card shapes match the design parameters in `DESIGN.md` on 100% of screens.
- **SC-002**: Screen transition and rendering times are kept under 300ms.
- **SC-003**: 100% of user input states and selections are preserved when rotating the screen or switching system themes.
- **SC-004**: Interactive components have a touch target area of at least 48x48dp.
- **SC-005**: Contrast ratio of body text is at least 4.5:1 in both light and dark modes.

## Assumptions

- We assume the existing domain use cases, repositories, and Room database structures do not require functional changes, only mapping to the newly styled presentation components.
- Standard vector icons are sourced from Jetpack Compose Material Icons or bundled XML assets.
- System translations are maintained in French and Arabic resource files.
- The 2D preview canvas is a simplified draw operation using Compose Canvas API.
