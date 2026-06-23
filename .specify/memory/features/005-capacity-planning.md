# Feature Brief: Capacity Planning

**Sequence**: #005 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #003 House Dimensions Wizard, #004 Breed Configuration & Reference Database
- **Unlocks**: #006 Ventilation & Climate Design, #009 Financial Dashboard, #010 Risk & Welfare Assessment
- **Parallelizable with**: None (requires both #003 and #004)

## Feature Description

Capacity Planning is the critical wizard step where house dimensions meet breed specifications to determine how many birds the facility can safely and legally hold. This calculation is at the heart of the application's value proposition — it replaces manual spreadsheet calculations with an instant, standards-aware density assessment.

Given the house floor area (from Step 1) and the selected breed's space-per-bird requirement (from Step 2), the system calculates the maximum bird capacity automatically. But capacity isn't just a simple division. EU welfare directives impose density limits based on total live weight per square meter at slaughter time, not just bird count. The system must factor in the breed's target slaughter weight to compute kg/m² density and compare it against EU thresholds.

The capacity result is displayed with a prominent color-coded density indicator bar. Green indicates safe compliance (≤ 33 kg/m² per EU standard density). Yellow signals a zone requiring advanced cooling and ventilation systems (> 33 to ≤ 39 kg/m²). Red warns that the configuration exceeds welfare safety margins (> 39 kg/m²). Users can manually adjust the bird count using an interactive slider, but the system provides immediate visual and textual warnings when density thresholds are breached.

This wizard step (Step 3) is where the consultant first sees whether the proposed house dimensions are viable for the intended flock size. It serves as both a design validation gate and a persuasion tool — when presenting to farmers, the color-coded indicator makes density compliance immediately understandable without technical jargon.

## Derived From

- **Product Spec**: §5.1 Core Feature 4 — Capacity Planning (max bird capacity from dimensions + breed, color-coded density indicator Green/Yellow/Red, warning alerts on manual override)
- **UI Spec**: §4.8 Step 3: Capacity Planning (HSL color-coded density warning bar, EU welfare standard thresholds: Green ≤ 33 kg/m², Yellow > 33–≤ 39, Red > 39 kg/m²)
- **Constitution**: Article 4.3 (Input Validation — capacity: non-negative integers), Article 2.5 (Documentation — complex business logic must reference EU directives)

## Acceptance Criteria Summary

- [ ] Maximum bird capacity is auto-calculated from house floor area and breed space-per-bird requirement
- [ ] Density indicator displays Green (≤ 33 kg/m²), Yellow (> 33–≤ 39 kg/m²), or Red (> 39 kg/m²) based on total live weight at slaughter per m²
- [ ] Users can manually adjust bird count via an interactive slider
- [ ] Warning alerts appear when manual adjustment pushes density beyond safe thresholds
- [ ] Capacity and density values update in real-time as the slider moves
- [ ] The calculated capacity persists to the project data for use by downstream features (ventilation, financials, risk)

## UI Components (if applicable)

- **CapacityPlanningStep**: Wizard Step 3 composable with auto-calculated capacity display and density bar
- **DensityIndicatorBar**: Color-coded horizontal bar (Green/Yellow/Red) with threshold labels and current position marker
- **CapacitySlider**: Interactive slider for manual bird count adjustment with track coloring that changes with density zones
- **DensityWarningAlert**: Inline warning card displayed when density exceeds safe limits

## Technical Hints

- EU Council Directive 2007/43/EC sets maximum stocking density at 33 kg/m² as baseline, extendable to 39 kg/m² with additional welfare conditions — these thresholds should be configurable constants, not hardcoded magic numbers
- Constitution Article 2.3 suggests value classes: consider `BirdCount`, `DensityKgPerSqm` to prevent unit confusion in calculations
- The capacity calculation use case (`CalculateCapacityUseCase`) is a pure domain function with no Android dependencies — ideal for thorough unit testing per Constitution Article 2.1

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
