# Feature Brief: Equipment Selection & Catalog

**Sequence**: #007 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System, #002 Project Management
- **Unlocks**: #008 2D Blueprint Canvas, #009 Financial Dashboard
- **Parallelizable with**: #006 Ventilation & Climate System Design

## Feature Description

Every broiler house requires a range of equipment beyond the ventilation system — feeding lines, watering systems, lighting fixtures, heating accessories, and cooling components. This feature provides a comprehensive equipment catalog and a wizard step for building the project's bill of materials (BOM).

The app ships with a preloaded equipment catalog organized into five categories: Feeding, Watering, Heating, Cooling, and Lighting. Each catalog item includes a name, description, category, default unit price, and technical specifications. Users browse the catalog via a searchable, category-filtered list and add items to their project's equipment list with custom quantities. Unit prices can be overridden to match local vendor pricing — a critical capability since equipment costs vary significantly between regions and suppliers.

Beyond the preloaded catalog, users can add custom equipment items that aren't in the standard catalog. This allows consultants to include specialized local products, proprietary systems, or miscellaneous accessories in the BOM and cost estimates. Custom items require a name, category, quantity, and unit price.

The Equipment Selection step (Wizard Step 5) presents the project's current equipment list alongside the catalog. Users add, remove, and adjust quantities of items. A running subtotal updates in real-time. The equipment list feeds directly into the Financial Dashboard for cost estimation and into the 2D Blueprint Canvas for spatial placement of feeding lines, watering lines, and other equipment.

The catalog is also accessible as a standalone reference screen (CATALOG_EQUIPMENT route) outside the wizard context, allowing users to browse available equipment and pricing.

## Derived From

- **Product Spec**: §5.1 Core Feature 6 — Equipment Selection & Catalog (pre-loaded catalog by category, add to project, custom quantities, override prices, custom equipment)
- **Product Spec**: §5.3 MoSCoW — Custom Equipment Catalog Creation (Could Have)
- **UI Spec**: §3 Navigation (CATALOG_EQUIPMENT route), §4.8 Step 5: Equipment Selection (watering cups, feeder lines, lighting, accessories, BOM)
- **Constitution**: Article 1.2 (Offline-First — preloaded catalog), Article 4.3 (Input Validation — prices: non-negative decimals, 2 decimal places)

## Acceptance Criteria Summary

- [ ] App ships with a preloaded equipment catalog organized by Feeding, Watering, Heating, Cooling, and Lighting categories
- [ ] Users can browse and search the catalog with category filtering
- [ ] Users can add catalog items to the project BOM with custom quantities and optional price overrides
- [ ] Users can add custom (user-defined) equipment items with name, category, quantity, and unit price
- [ ] Project equipment list displays a running cost subtotal that updates in real-time
- [ ] Equipment data persists to Room and is available for the Financial Dashboard and Blueprint Canvas
- [ ] Standalone Equipment Catalog screen is accessible from main navigation for reference browsing

## UI Components (if applicable)

- **EquipmentSelectionStep**: Wizard Step 5 composable with project BOM list and catalog browser
- **EquipmentCatalogScreen**: Standalone browsable catalog with search and category filter tabs
- **EquipmentItemCard**: Card displaying equipment name, category, specs, and price with add/quantity controls
- **CustomEquipmentDialog**: Bottom sheet for creating user-defined equipment items
- **BomSummaryPanel**: Running total panel showing item count and aggregate cost

## Technical Hints

- Constitution Article 4.3 mandates price validation: non-negative decimals with 2 decimal places
- Equipment catalog seed data resides in `assets/seed/` alongside breed data, loaded via Room pre-populate
- The BOM is a join table between Project and Equipment entities, with quantity and price override columns
- Custom equipment items are user-created rows in the same Equipment table but flagged as `isCustom = true`

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
