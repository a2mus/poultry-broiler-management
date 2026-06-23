# Feature Brief: Risk & Welfare Assessment

**Sequence**: #010 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Large

## Dependencies

- **Requires**: #005 Capacity Planning, #006 Ventilation & Climate System Design
- **Unlocks**: #011 Enhancement & Upgrade Planning, #012 PDF Export & Reporting
- **Parallelizable with**: #009 Financial Dashboard

## Feature Description

The Risk & Welfare Assessment feature addresses the regulatory and safety dimension of broiler house design. EU animal welfare directives impose strict standards on stocking density, ventilation, environmental controls, and biosecurity. Consultants operating in EU-aligned markets (including Algeria, which follows EU standards for export compliance) need a structured way to evaluate whether a house design meets these requirements — and to identify specific gaps.

The feature computes three types of risk scores. First, a **chicken mortality risk score** (0–100) estimates flock loss probability based on ventilation adequacy (is the configured CFM sufficient for the bird count and climate?), insulation quality (does the wall/roof insulation prevent heat/cold stress?), and stocking density (is the kg/m² within safe limits?). Second, an **environmental risk assessment** evaluates heat stress and cold stress probabilities relative to the project's climate zone statistics. Third, a **EU Welfare Compliance evaluation** performs a gap analysis against specific directive checkpoints.

The compliance evaluation uses a multi-checkbox checklist covering mandatory welfare areas: biosecurity entry controls, medication/vaccine registry requirements, stocking density standards, and emergency ventilation backup systems. Each checkpoint is scored as compliant, partially compliant, or non-compliant. The aggregate produces a compliance percentage (e.g., "85% Conforme") and an overall pass/fail status that appears as a badge on the project card on the Home Screen.

The risk dashboard displays a radial gauge for the overall risk level (Low/Medium/High/Critical), categorized mortality risk breakdown cards with severity badges, environmental factor metric cards (temperature, humidity, air quality status), and the compliance checklist. This is Dashboard Tab 3 (Risk Assessment), and its outputs feed into the Enhancement module and PDF reports.

## Derived From

- **Product Spec**: §5.1 Core Feature 9 — Risk & Welfare Assessment (mortality risk score 0–100, EU Welfare Compliance gap analysis, environmental risk scoring)
- **Product Spec**: §5.3 MoSCoW — EU Welfare Directive Compliance (Must Have)
- **UI Spec**: §4.4 Risk Assessment Dashboard Tab (Stitch screens: `a6d82ba1`, `19cc2b50`), radial gauge, mortality breakdown, environmental factors grid, EU compliance checklist
- **Constitution**: Article 2.5 (Document complex risk calculation models, reference EU directives)

## Acceptance Criteria Summary

- [ ] Mortality risk score (0–100) is calculated from ventilation adequacy, insulation quality, and stocking density
- [ ] Environmental risk scores evaluate heat stress and cold stress probability based on climate zone data
- [ ] EU Welfare Compliance checklist covers biosecurity, medication registry, density standards, and emergency ventilation
- [ ] Each compliance checkpoint displays compliant/partially compliant/non-compliant status
- [ ] Overall compliance percentage and pass/fail badge are computed and displayed
- [ ] Radial risk gauge displays overall risk level (Low/Medium/High/Critical) with contextual warning bar
- [ ] Compliance score appears on the project card on the Home Screen

## UI Components (if applicable)

- **RiskAssessmentTab**: Dashboard Tab 3 composable with risk gauge, mortality breakdown, environmental grid, and compliance checklist
- **RiskRadialGauge**: SVG/Canvas radial gauge showing risk level (0–100) with color zones (Low=Green, Medium=Yellow, High=Orange, Critical=Red)
- **MortalityBreakdownCards**: Categorized stress factor cards (Thermal Stress, Viral Infection, Water Quality) with severity badges
- **EnvironmentalFactorCards**: Bento grid cards for Temperature, Humidity, and Air Quality with pulsing status dots
- **WelfareComplianceChecklist**: Multi-checkbox card with compliance percentage indicator badge and mandatory checkpoint items

## Technical Hints

- EU Council Directive 2007/43/EC is the primary reference for welfare compliance checkpoints — risk calculation logic must cite this in inline comments per Constitution Article 2.5
- Risk scoring models are estimative (per product-spec §8 Constraints) — they are mathematical models based on reference tables, not real-time sensor data
- The compliance pass/fail status should be stored on the Project entity so it can be displayed on the Home Screen project card without recalculation
- Constitution Article 3.3 requires WCAG AAA contrast (7:1) for critical numerical warnings in the risk display

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
