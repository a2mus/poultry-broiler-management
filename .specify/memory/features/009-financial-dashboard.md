# Feature Brief: Financial Dashboard & Cost Estimation

**Sequence**: #009 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Large

## Dependencies

- **Requires**: #005 Capacity Planning, #007 Equipment Selection & Catalog
- **Unlocks**: #011 Enhancement & Upgrade Planning, #012 PDF Export & Reporting
- **Parallelizable with**: #010 Risk & Welfare Assessment

## Feature Description

The Financial Dashboard transforms the broiler house design from a technical blueprint into a business case. Consultants use financial projections to justify construction investments or upgrades to farmers, and farmers use them to evaluate return on investment before committing capital. This feature provides comprehensive cost estimation, revenue projection, and ROI analysis.

The dashboard is organized into two major cost categories. Capital Expenditure (CapEx) aggregates all one-time costs: construction materials (derived from house dimensions, wall/floor materials, and roofing), equipment purchases (from the BOM in Feature #007), and installation labor. Operating Expenditure (OpEx) estimates recurring per-cycle costs: feed (calculated from bird count, breed FCR, and growth days), electricity consumption (from ventilation and lighting equipment specifications), labor (based on flock size and regional wage estimates), and overheads.

A pie chart breaks down the cost distribution across categories, making it easy to identify the largest cost drivers. Revenue projections are calculated from the bird capacity, target slaughter weight, and local market price per kilogram. The system computes per-cycle metrics (revenue, profit, cost per bird, revenue per bird) and annual metrics (total revenue, total profit) based on the number of flock cycles per year.

The ROI analysis shows the payback period in months — how long until cumulative profits recover the CapEx investment. An interactive slider lets users adjust the expected number of flock cycles per year (4 to 7), dynamically updating all annual projections and the ROI timeline. This interactive simulation is a key selling feature during client presentations.

All financial values are displayed using the project's currency (DZD or EUR) with locale-appropriate formatting. The dashboard is the second tab (Financials) on the Project Dashboard.

## Derived From

- **Product Spec**: §5.1 Core Feature 8 — Financial Dashboard & Cost Estimation (construction/equipment costs, pie chart, operational costs, cycle/annual revenue/profit, ROI payback, interactive slider)
- **UI Spec**: §4.5 Financial Analysis Dashboard Tab (Stitch screens: `94a19a0f`, `8f609b3d`), CapEx/OpEx bento grid, operating cost breakdown cards, ROI chart, flock cycle slider
- **Constitution**: Article 2.5 (Document complex financial formulas), Article 8.2 (Locale-aware numeric/currency formatting)

## Acceptance Criteria Summary

- [ ] CapEx is calculated from construction materials, equipment BOM costs, and installation estimates
- [ ] OpEx per cycle is calculated from feed (bird count x FCR x feed price), electricity, labor, and overheads
- [ ] Pie chart displays cost distribution breakdown across major categories
- [ ] Revenue and profit projections are calculated per cycle and annually based on bird capacity, slaughter weight, and market price
- [ ] ROI payback period (in months) is displayed based on cumulative profits vs. CapEx
- [ ] Interactive slider adjusts flock cycles per year (4–7) and dynamically updates all annual projections
- [ ] All financial values use locale-appropriate currency formatting (DZD/EUR)

## UI Components (if applicable)

- **FinancialDashboardTab**: Dashboard Tab 2 composable with CapEx/OpEx summary, cost breakdown, ROI chart, and cycle slider
- **CapExSummaryCard**: Total infrastructure cost with progress bar showing budget allocation
- **OpExSummaryCard**: Per-cycle operating cost with trend indicators
- **CostBreakdownCards**: Individual cards for Feed, Electricity, and Labor costs with details
- **RoiProjectionChart**: Area chart tracing cost payback with break-even point marker
- **FlockCycleSlider**: Range slider (4–7 cycles/year) with dynamic calculation update

## Technical Hints

- Financial calculations are pure domain logic — the `CalculateFinancialsUseCase` should be in the Domain layer with comprehensive unit tests per Constitution Article 2.1
- Constitution Article 8.2 requires `DateTimeFormatter` with locale-aware patterns and proper decimal/currency separators
- Feed cost formula: `birdCount × averageFeedPerBird(kg) × feedPricePerKg` where `averageFeedPerBird = slaughterWeight × FCR`
- Constitution Article 2.5 mandates inline comments explaining each financial model and its assumptions

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
