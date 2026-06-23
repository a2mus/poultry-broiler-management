# Feature Brief: House Dimensions Wizard Step

**Sequence**: #003 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System, #002 Project Management
- **Unlocks**: #005 Capacity Planning, #006 Ventilation & Climate Design, #008 2D Blueprint Canvas
- **Parallelizable with**: #004 Breed Configuration & Reference Database

## Feature Description

The House Dimensions Wizard Step is the first step in the multi-step project design wizard. When a consultant or farmer creates a new broiler house project, this is where they define the physical structure of the building. Accurate dimensions are the foundation for every subsequent calculation — capacity, ventilation rates, equipment spacing, and financial estimates all derive from the house geometry.

The user enters the building length, width, and wall height in meters using numeric input fields with persistent trailing unit labels. They select the roof type from three options — Flat, Pitched (Gable), or Arched — with an additional ridge height field appearing for pitched roofs. Wall material is chosen via a segmented selector with visual icons: Block, Steel, or Prefab. Floor type (Concrete, Dirt, Slat) and insulation parameters (type and thickness) are also configured here.

As the user enters dimensions, a real-time calculated field displays the total floor area in square meters. Below the form fields, a live 2D preview canvas renders a scaled top-down outline of the building with ruler markers, giving immediate visual feedback. This preview is a simplified version of the full blueprint canvas; it shows only the building envelope without equipment placement.

The wizard step includes a progress indicator badge showing "Step 1/6" and navigation buttons (Previous/Next) at the bottom. All numeric inputs are validated for positive values and reasonable upper bounds (e.g., length <= 500m). Invalid entries display inline error messages. Data persists to Room immediately on field changes so that no work is lost if the user navigates away.

## Derived From

- **Product Spec**: §5.1 Core Feature 2 — House Dimension Setup (length, width, wall height, roof type, wall material, floor type, insulation, real-time area calculation)
- **UI Spec**: §4.2 House Dimensions Wizard Step (Stitch screens: `577985ee`, `5bcbb9ba`), §2.3 Form & Input Controls
- **Constitution**: Article 4.3 (Input Validation — dimensions: positive numbers, length <= 500m), Article 1.2 (Offline-First), Article 3.1 (Design Tokens)

## Acceptance Criteria Summary

- [ ] User can input length, width, and wall height in meters with numeric keypad and trailing unit labels
- [ ] Roof type selection (Flat, Pitched, Arched) works via segmented controls; pitched selection reveals ridge height field
- [ ] Wall material (Block, Steel, Prefab) and floor type (Concrete, Dirt, Slat) selectors function with visual icons
- [ ] Total floor area calculates and displays in real-time as dimensions change
- [ ] A live 2D preview canvas renders the building outline to scale with ruler markers
- [ ] Input validation prevents non-positive values and values exceeding reasonable bounds, displaying inline errors
- [ ] Wizard progress indicator shows "Step 1/6" and Previous/Next navigation works correctly

## UI Components (if applicable)

- **WizardStepIndicator**: Progress badge showing current step (e.g., "Etape 1/4") per ui-spec §4.2
- **StructuralMeasuresCard**: Bento-grid input card with Length, Width, Wall Height fields and calculated area display
- **WallInsulationSelector**: Radio selectors with illustrative icon badges for Prefab/Block/Steel per ui-spec §4.2
- **RoofTypeSelector**: Segmented checkmark cards for Gable/Flat per ui-spec §4.2
- **DimensionPreviewCanvas**: Simplified 2D top-down building outline with ruler markers
- **WizardNavigationBar**: Bottom bar with Previous/Next buttons (RTL-mirrored for Arabic)

## Technical Hints

- Constitution Article 4.3 mandates dual-layer validation: Compose field validators AND Domain Use Case preconditions
- Dimension data should be stored as a sub-entity of the Project, updated on each field change for crash resilience
- The 2D preview canvas reuses the Compose Canvas API infrastructure established in #001; it is a simpler subset of the full blueprint in #008
- Constitution Article 2.3 suggests using value classes for domain primitives (e.g., `Meters`, `SquareMeters`) to prevent unit confusion

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
