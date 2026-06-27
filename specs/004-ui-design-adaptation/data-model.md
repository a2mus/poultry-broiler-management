# Data Model: UI Design Adaptation

This document defines the data entities, attributes, relationships, and validation constraints for the presentation layer models introduced or updated during the UI design adaptation.

---

## 1. Presentation Entities & Models

### `AppTheme`
Represents the configuration for colors, typography, shapes, and elevations.
- **Attributes**:
  - `colors`: `ColorScheme` (Light/Dark color palettes)
  - `typography`: `Typography` (Outfit for display, Inter for body/numeric)
  - `shapes`: `Shapes` (Corner radiuses: 16dp cards, 24dp buttons, 8dp badges, 28dp dialogs)
  - `spacing`: `PoultrySpacing` (Micro, small, medium, large values)

### `ProjectOverview`
Represents a summarized project metadata for display on the home dashboard project cards.
- **Attributes**:
  - `id`: `String` (Unique identifier)
  - `name`: `String` (Project name)
  - `location`: `String` (City/Region name)
  - `country`: `String` (Country code/name, default: "Algérie")
  - `imageUrl`: `String` (Banner image representative of the region)
  - `status`: `ProjectStatus` (Enum: `DRAFT`, `COMPLETED`, `PENDING`)
  - `capacity`: `Int` (Number of birds)
  - `ventilationState`: `String` (Status label, e.g., "OPTIMAL")
  - `complianceScore`: `Int` (Percentage value from 0 to 100)

### `InsulationOption`
Represents a selectable wall insulation configuration card on the dimensions step.
- **Attributes**:
  - `type`: `InsulationType` (Enum: `PREFAB`, `BLOCK`, `STEEL`)
  - `title`: `String` (Display title)
  - `description`: `String` (Detailed subtext)
  - `iconResId`: `Int` (Vector resource identifier)

### `RoofOption`
Represents a selectable roof type option card on the dimensions step.
- **Attributes**:
  - `type`: `RoofType` (Enum: `GABLE`, `FLAT`, `ARCHED`)
  - `title`: `String` (Display title)
  - `description`: `String` (Detailed subtext)
  - `iconResId`: `Int` (Vector resource identifier)

### `RiskAssessment`
Represents the environmental parameters and welfare compliance status for the risk dashboard.
- **Attributes**:
  - `safetyScore`: `Int` (Safety compliance percentage from 0 to 100)
  - `temperature`: `Float` (Sensor reading in °C)
  - `humidity`: `Float` (Sensor reading in %)
  - `airQuality`: `Int` (AQI sensor reading)
  - `tempStatus`: `StatusColor` (Enum: `GREEN`, `YELLOW`, `RED`)
  - `humidityStatus`: `StatusColor` (Enum: `GREEN`, `YELLOW`, `RED`)
  - `airQualityStatus`: `StatusColor` (Enum: `GREEN`, `YELLOW`, `RED`)
  - `checklistItems`: `List<ComplianceCheck>` (List of checkboxes)

### `ComplianceCheck`
Represents a single compliance checkpoint on the EU welfare checklist.
- **Attributes**:
  - `id`: `String` (Unique identifier)
  - `label`: `String` (Localized text description)
  - `isChecked`: `Boolean` (Active state)
  - `isMandatory`: `Boolean` (True if required for general compliance validation)

### `FinancialSummary`
Represents CapEx/OpEx metrics and flock simulations.
- **Attributes**:
  - `capexTotal`: `Double` (Total initial infrastructure cost in DZD)
  - `opexTotal`: `Double` (Ongoing costs per cycle in DZD)
  - `feedCost`: `Double` (Cost of feeds in DZD)
  - `electricityCost`: `Double` (Electricity cost in DZD)
  - `laborCost`: `Double` (Labor cost in DZD)
  - `flockCyclesPerYear`: `Int` (Simulation variable, defaults to 5)
  - `estimatedAnnualProfit`: `Double` (Live calculated profit in DZD)

### `UpgradeProposal`
Represents technical recommendations on the Improvements tab.
- **Attributes**:
  - `id`: `String` (Unique identifier)
  - `title`: `String` (Title of the upgrade)
  - `priority`: `UpgradePriority` (Enum: `CRITICAL`, `HIGH`, `MEDIUM`)
  - `currentState`: `String` (Description of current system)
  - `proposedState`: `String` (Description of proposed system)
  - `estimatedCost`: `Double` (Upgrade cost in DZD)
  - `annualSavings`: `Double` (Savings generated per year in DZD)
  - `roiMonths`: `Int` (Calculated payback period in months)

---

## 2. Validation Constraints

- **Stocking Density limits**:
  - Density MUST trigger warning flags if exceeding `33 kg/m²` (Yellow warning).
  - Density MUST trigger critical failure flags if exceeding `39 kg/m²` (Red alert).
- **Dimension values**:
  - Length: `0 < value <= 500` meters
  - Width: `0 < value <= 100` meters
  - Wall Height: `0 < value <= 15` meters
  - Ridge Height: `0 < value <= 20` meters
  - Insulation Thickness: `0 <= value <= 500` millimeters
- **Financial values**:
  - Flock cycles: `4 <= value <= 7` cycles/year
