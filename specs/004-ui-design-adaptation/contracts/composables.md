# UI Component Contracts: UI Design Adaptation

**Feature**: 004-ui-design-adaptation

**Date**: 2026-06-27

---

## 1. Global Themes & Foundation

### Color.kt Updates
Provides the HSL color variables specified in `agri_integrity_pro/DESIGN.md`:
```kotlin
// Light Mode (Field Glare Reduction)
val ForestTealLightPrimary = Color(0xFF00685C)
val ForestTealLightOnPrimary = Color(0xFFFFFFFF)
val ForestTealLightPrimaryContainer = Color(0xFF1E8275)
val ForestTealLightOnPrimaryContainer = Color(0xFFF2FFFB)
val ForestTealLightSecondary = Color(0xFF865300)
val ForestTealLightSecondaryContainer = Color(0xFFFCAE49)
val ForestTealLightOnSecondaryContainer = Color(0xFF6E4300)
val ForestTealLightBackground = Color(0xFFF7F9FF)
val ForestTealLightSurface = Color(0xFFF7F9FF)
val ForestTealLightOnSurface = Color(0xFF131D24)
val ForestTealLightSurfaceVariant = Color(0xFFDAE3EE)
val ForestTealLightOnSurfaceVariant = Color(0xFF3E4946)
val ForestTealLightOutline = Color(0xFF6E7976)
val ForestTealLightOutlineVariant = Color(0xFFBDC9C5)
val ForestTealLightSuccess = Color(0xFF229954)
val ForestTealLightWarning = Color(0xFFD4AC0D)
val ForestTealLightDanger = Color(0xFFCB4335)

// Dark Mode (Sleek Carbon)
val SleekCarbonDarkPrimary = Color(0xFF2ECC71)
val SleekCarbonDarkOnPrimary = Color(0xFF0E141B)
val SleekCarbonDarkSecondary = Color(0xFFF39C12)
val SleekCarbonDarkBackground = Color(0xFF0C1013)
val SleekCarbonDarkSurface = Color(0xFF171F26)
val SleekCarbonDarkOnSurface = Color(0xFFEDEFF1)
val SleekCarbonDarkSurfaceVariant = Color(0xFF242F3A)
val SleekCarbonDarkSuccess = Color(0xFF27AE60)
val SleekCarbonDarkWarning = Color(0xFFF1C40F)
val SleekCarbonDarkDanger = Color(0xFFE74C3C)
```

### Type.kt Updates
Loads and configures the Outfit and Inter fonts.
```kotlin
val OutfitFontFamily = FontFamily(
    Font(R.font.outfit_semibold, FontWeight.SemiBold),
    Font(R.font.outfit_bold, FontWeight.Bold)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold)
)
```

---

## 2. Bento Grid Dashboard Components

### ProjectCard (Home Screen)
Renders a single project summary card in the Bento Grid.
```kotlin
@Composable
fun ProjectCard(
    project: ProjectOverview,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
)
```
- **Visuals**:
  - Image thumbnail using the region's agricultural backdrop.
  - Corner radius: `16.dp` with Level 1 elevation shadow.
  - Scale micro-interaction down to `98%` on long-press.
  - Localized status badges (French and Arabic).

### PerformanceSummaryCard (Home Screen)
Primary highlights banner displaying current FCR and production indices.
```kotlin
@Composable
fun PerformanceSummaryCard(
    onOpenReports: () -> Unit,
    modifier: Modifier = Modifier,
)
```
- **Visuals**: Primary Forest Teal container with white text and `Outfit` typography display headers.

---

## 3. Dimensions Wizard Custom Components

### RoofTypeSelector (Wizard Step 1)
```kotlin
@Composable
fun RoofTypeSelector(
    selectedType: RoofType?,
    onSelect: (RoofType) -> Unit,
    modifier: Modifier = Modifier,
)
```
- **Visuals**: Cards with Flat or Pitched labels and checkmark indicator overlays. Gable/Flat buttons with `change_history` and `horizontal_rule` icons.

### DimensionPreviewCanvas (Wizard Step 1)
```kotlin
@Composable
fun DimensionPreviewCanvas(
    length: Double,
    width: Double,
    modifier: Modifier = Modifier,
)
```
- **Visuals**: Scaled rectangle blueprint of the house with outline borders and edge labels indicating measurements in meters.

---

## 4. Risk Assessment Dashboard Components

### RiskScoreGauge (Risk Assessment Tab)
Radial meter displaying current safety compliance score.
```kotlin
@Composable
fun RiskScoreGauge(
    score: Int,  // 0 to 100
    modifier: Modifier = Modifier,
)
```
- **Visuals**: SVG-style circular arc transitioning color indicators depending on risk thresholds.

---

## 5. Financial & Enhancements Tab Components

### FlockCyclesSimulator (Financial Tab)
```kotlin
@Composable
fun FlockCyclesSimulator(
    currentCycles: Int,
    onCyclesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
)
```
- **Visuals**: Interactive slider control allowing steps between 4 and 7 cycles, with a text display showing Estimated Annual Profit.

### UpgradeRecommendationCard (Enhancements Tab)
```kotlin
@Composable
fun UpgradeRecommendationCard(
    proposal: UpgradeProposal,
    onAddToPlan: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
)
```
- **Visuals**: Side-by-side comparison boxes showing Current vs. Proposed states (e.g. R-12 to R-28 insulation). Red/yellow/green side highlight border indicating priority level.
