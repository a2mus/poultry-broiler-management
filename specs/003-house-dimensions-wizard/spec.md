# Feature Specification: House Dimensions Wizard Step

**Feature Branch**: `003-house-dimensions-wizard`

**Created**: 2026-06-24

**Status**: Draft

**Input**: User description: "The House Dimensions Wizard Step is the first step in the multi-step project design wizard. When a consultant or farmer creates a new broiler house project, this is where they define the physical structure of the building."

## Clarifications

### Session 2026-06-24

- Q: Which fields are required to advance to Step 2? → A: All fields are mandatory — dimensions (length, width, wall height), roof type (+ ridge height if pitched), wall material, floor type, insulation type, and insulation thickness must all be filled before the user can proceed.
- Q: What compass granularity for house orientation? → A: 8 compass points (N, NE, E, SE, S, SW, W, NW).
- Q: What are the insulation type options? → A: 4 options — None, Polystyrene, Polyurethane, Mineral Wool.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Enter Building Dimensions (Priority: P1)

A consultant creates a new broiler house project and reaches the first wizard step. They enter the building length (e.g., 120m), width (e.g., 14m), and wall height (e.g., 3m) using numeric input fields. Each field displays a persistent trailing unit label ("m"). As soon as the width and length are filled in, a calculated field below instantly shows the total floor area (1,680 m²). The consultant sees a live 2D top-down preview of the building outline update in real time, with ruler markers indicating the entered dimensions.

**Why this priority**: Dimensions are the foundational data point for every downstream calculation in the application — capacity, ventilation, equipment spacing, and financial projections all depend on accurate house geometry. Without this, the entire wizard is non-functional.

**Independent Test**: Can be fully tested by opening a new project, entering length/width/height values, and verifying the calculated floor area and live 2D preview update correctly.

**Acceptance Scenarios**:

1. **Given** the user is on the House Dimensions wizard step, **When** they enter length = 100, width = 12, and wall height = 3, **Then** the floor area displays "1,200 m²" and the 2D preview renders a scaled rectangle with ruler markers showing 100m x 12m.
2. **Given** the user has entered dimensions, **When** they change the width from 12 to 15, **Then** the floor area instantly recalculates to "1,500 m²" and the 2D preview resizes proportionally.
3. **Given** the user enters dimensions, **When** they navigate away and return to this step, **Then** all previously entered values are preserved.

---

### User Story 2 - Select Roof Type with Conditional Fields (Priority: P1)

The consultant selects the roof type for the broiler house from three options presented as segmented controls: Flat, Pitched (Gable), or Arched. When "Pitched (Gable)" is selected, an additional input field for ridge height appears with smooth animation. For Flat and Arched selections, the ridge height field remains hidden.

**Why this priority**: Roof type directly affects the building's internal volume, which is critical for ventilation rate calculations and structural cost estimates. The conditional field display is a core interaction pattern reused throughout the wizard.

**Independent Test**: Can be fully tested by selecting each roof type option and verifying that the ridge height field appears only for Pitched, while the 2D preview updates to reflect the selected roof silhouette.

**Acceptance Scenarios**:

1. **Given** the user is on the dimensions step, **When** they select "Flat" roof type, **Then** no additional roof fields appear and the selection is visually highlighted with a checkmark.
2. **Given** the user is on the dimensions step, **When** they select "Pitched (Gable)" roof type, **Then** a ridge height input field appears with a trailing "m" label and the previous selection is deselected.
3. **Given** the user has selected "Pitched (Gable)" and entered a ridge height, **When** they switch to "Arched", **Then** the ridge height field disappears and the ridge height value is cleared.

---

### User Story 3 - Select Wall Material and Floor Type (Priority: P2)

The consultant selects the wall material from a segmented selector with visual icons: Block, Steel, or Prefab. They also select the floor type from options: Concrete, Dirt, or Slat. Each selector displays an illustrative icon badge alongside the label text.

**Why this priority**: Material selections influence structural cost calculations and insulation requirements. While important for downstream features, they do not block dimension entry or area calculations.

**Independent Test**: Can be fully tested by selecting each wall material and floor type option, verifying visual feedback (icon highlighting, checkmark), and confirming the selection persists when navigating away.

**Acceptance Scenarios**:

1. **Given** the user is on the dimensions step, **When** they tap "Block" in the wall material selector, **Then** the Block option is highlighted with a checkmark and its icon badge is visually active.
2. **Given** the user has selected "Steel" wall material, **When** they tap "Prefab", **Then** "Steel" is deselected and "Prefab" becomes the active selection.
3. **Given** the user is on the dimensions step, **When** they select "Slat" floor type, **Then** the Slat option is visually highlighted and the selection is immediately persisted.

---

### User Story 4 - Configure Insulation Parameters (Priority: P2)

The consultant configures the insulation for the broiler house by selecting the insulation type and entering the thickness in millimeters. These parameters feed into thermal performance calculations in later wizard steps.

**Why this priority**: Insulation data is needed for ventilation and climate design calculations (Feature #006) but does not block the core dimension entry or immediate area calculations.

**Independent Test**: Can be fully tested by selecting an insulation type, entering thickness, and verifying the values persist correctly.

**Acceptance Scenarios**:

1. **Given** the user is on the dimensions step, **When** they select an insulation type and enter thickness = 50, **Then** the values are displayed with a trailing "mm" label and persisted immediately.
2. **Given** the user has configured insulation, **When** they navigate to the next step and return, **Then** the insulation type and thickness values are preserved.

---

### User Story 5 - Set House Orientation (Priority: P1)

The consultant sets the orientation of the poultry house by selecting the compass direction the building's long axis faces. Orientation is a critical factor in house heating and solar exposure management — it determines how sunlight hits the walls and roof throughout the day, directly affecting internal temperatures and ventilation requirements. The orientation is selected via a compass-style selector or segmented control.

**Why this priority**: Orientation directly impacts heating and ventilation calculations (Feature #006). Incorrect or missing orientation data would lead to inaccurate climate design outputs for the building.

**Independent Test**: Can be fully tested by selecting an orientation, verifying the 2D preview rotates or annotates the direction indicator, and confirming the value persists when navigating away.

**Acceptance Scenarios**:

1. **Given** the user is on the dimensions step, **When** they select an orientation (e.g., North-South), **Then** the selection is visually highlighted and the 2D preview displays a compass/direction indicator reflecting the chosen orientation.
2. **Given** the user has selected an orientation, **When** they navigate to the next step and return, **Then** the orientation selection is preserved.
3. **Given** no orientation is selected, **When** the user taps "Next", **Then** the "Next" button remains disabled and an inline message indicates orientation is required.

---

### User Story 6 - Input Validation and Error Feedback (Priority: P1)

The system validates all numeric inputs in real time. Non-positive values and values exceeding reasonable upper bounds (e.g., length > 500m, width > 100m, height > 15m) trigger inline error messages displayed directly below the affected field. All fields on this wizard step are mandatory — the "Next" button is disabled when any validation error is present or any field remains empty.

**Why this priority**: Invalid dimensions would cascade errors through all downstream calculations. Preventing bad data at the entry point is essential for data integrity and user trust.

**Independent Test**: Can be fully tested by entering invalid values (0, negative numbers, values exceeding bounds) and verifying inline error messages appear, the "Next" button is disabled, and correcting the values clears the errors.

**Acceptance Scenarios**:

1. **Given** the user is on the dimensions step, **When** they enter length = 0, **Then** an inline error message "Value must be greater than 0" appears below the length field and the "Next" button is disabled.
2. **Given** the user is on the dimensions step, **When** they enter length = 600, **Then** an inline error message "Value must not exceed 500m" appears below the length field.
3. **Given** the user has a validation error displayed, **When** they correct the value to a valid number within bounds, **Then** the error message disappears and the "Next" button becomes enabled.
4. **Given** the user selects "Pitched (Gable)" roof type, **When** they leave the ridge height field empty and tap "Next", **Then** an inline error message "Ridge height is required for pitched roofs" appears.

---

### User Story 7 - Wizard Navigation and Progress (Priority: P2)

The wizard step displays a progress indicator badge showing "Step 1/6" at the top. Navigation buttons at the bottom allow moving to the next step. The "Previous" button is disabled or hidden on this first step. The "Next" button advances to the next wizard step only when all required fields are valid. Navigation buttons are mirrored for Arabic RTL layouts.

**Why this priority**: Navigation and progress indication are essential UX elements but are not unique to this step — this pattern will be shared across all wizard steps.

**Independent Test**: Can be fully tested by verifying the progress indicator shows "Step 1/6", the Previous button is inactive, the Next button validates before advancing, and the layout mirrors correctly in RTL mode.

**Acceptance Scenarios**:

1. **Given** the user is on the first wizard step, **When** the step loads, **Then** a progress badge displays "Step 1/6" and the "Previous" button is disabled or hidden.
2. **Given** all required fields are valid, **When** the user taps "Next", **Then** the wizard advances to Step 2.
3. **Given** the app language is set to Arabic, **When** the user views the wizard step, **Then** the Next button appears on the left side and Previous on the right side (RTL mirroring).

---

### Edge Cases

- What happens when the user rotates the device mid-entry? All entered values and selections must be preserved across configuration changes.
- What happens when the user enters extremely small dimensions (e.g., length = 0.1m, width = 0.1m)? The 2D preview should still render a visible outline, and the floor area should display correctly (0.01 m²).
- What happens when the user rapidly switches between roof types? The conditional ridge height field should animate smoothly without visual glitches, and only the current selection's data should be retained.
- What happens when the user enters decimal values with many decimal places (e.g., 12.123456789)? The system should accept the value but display the calculated area rounded to 2 decimal places.
- What happens if Room persistence fails (e.g., storage full)? The user should see an error message indicating data could not be saved, and the entered values should remain in the form so they can retry.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide numeric input fields for building length, width, and wall height, each displaying a persistent trailing unit label ("m").
- **FR-002**: System MUST calculate and display the total floor area (length x width) in square meters in real time as the user modifies dimension values.
- **FR-003**: System MUST provide a segmented control selector for roof type with three options: Flat, Pitched (Gable), and Arched, allowing single selection only.
- **FR-004**: System MUST display an additional ridge height input field (with trailing "m" label) when the user selects "Pitched (Gable)" roof type, and hide it for other selections.
- **FR-005**: System MUST provide a segmented selector with visual icon badges for wall material with three options: Block, Steel, and Prefab.
- **FR-006**: System MUST provide a selector with visual icon badges for floor type with three options: Concrete, Dirt, and Slat.
- **FR-007**: System MUST provide a selector for insulation type with four options: None, Polystyrene, Polyurethane, and Mineral Wool, and a numeric input field for insulation thickness entry (in millimeters with trailing "mm" label). When "None" is selected, the thickness field MUST be hidden and its value cleared.
- **FR-008**: System MUST render a live 2D top-down preview canvas showing the building outline to scale with ruler markers reflecting the entered dimensions.
- **FR-009**: System MUST validate all numeric inputs for positive values (greater than zero) at both the form field level and the domain logic level.
- **FR-010**: System MUST validate dimension upper bounds: length not exceeding 500m, width not exceeding 100m, wall height not exceeding 15m, ridge height not exceeding 20m, and insulation thickness not exceeding 500mm.
- **FR-011**: System MUST display inline error messages directly below each invalid field, describing the specific validation failure.
- **FR-012**: System MUST disable the "Next" navigation button when any validation error is present on the form OR any mandatory field remains empty. All fields on this step are mandatory: length, width, wall height, roof type (+ ridge height if Pitched), wall material, floor type, insulation type, insulation thickness (unless insulation type is "None"), and house orientation.
- **FR-013**: System MUST display a progress indicator badge showing "Step 1/6" at the top of the wizard step.
- **FR-014**: System MUST provide "Previous" and "Next" navigation buttons at the bottom of the step, with "Previous" disabled or hidden on this first step.
- **FR-015**: System MUST persist all entered dimension data and material selections to local storage immediately on each field change for crash resilience.
- **FR-016**: System MUST preserve all entered data when the user navigates away from and returns to this wizard step.
- **FR-017**: System MUST mirror navigation button positions (Next/Previous) for right-to-left language layouts (Arabic).
- **FR-018**: System MUST use numeric keypad input mode for all numeric fields to facilitate data entry.
- **FR-019**: System MUST clear the ridge height value when the user switches away from "Pitched (Gable)" roof type to prevent stale data.
- **FR-020**: System MUST update the 2D preview canvas in real time as dimension values change, maintaining proportional scaling with ruler markers.
- **FR-021**: System MUST display the calculated floor area rounded to 2 decimal places.
- **FR-022**: System MUST preserve all entered values across device orientation changes (configuration changes).
- **FR-023**: System MUST provide a selector for house orientation using 8 compass points (N, NE, E, SE, S, SW, W, NW) representing the direction of the building's long axis. Orientation is mandatory and directly affects heating and ventilation calculations.

### Key Entities

- **HouseDimensions**: Represents the physical measurements of the broiler house — length, width, wall height, and optionally ridge height (when roof is pitched). All values expressed as positive decimal numbers in meters.
- **RoofType**: A finite set of roof configurations — Flat, Pitched (Gable), or Arched. Determines whether additional height parameters are required.
- **WallMaterial**: A finite set of construction material choices — Block, Steel, or Prefab. Affects structural cost calculations downstream.
- **FloorType**: A finite set of flooring options — Concrete, Dirt, or Slat. Influences capacity and hygiene calculations.
- **InsulationConfig**: Represents insulation parameters — type (None, Polystyrene, Polyurethane, or Mineral Wool) and thickness (in millimeters). When type is "None", thickness is not applicable. Feeds into thermal performance and ventilation calculations.
- **HouseOrientation**: The compass direction of the building's long axis, selected from 8 compass points: N, NE, E, SE, S, SW, W, NW. A critical factor in heat gain from solar exposure, directly affecting heating and ventilation design.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can enter all house dimensions (length, width, wall height) and see the calculated floor area within 5 seconds of reaching the wizard step.
- **SC-002**: The 2D preview canvas updates within 300 milliseconds of any dimension field change, providing visually smooth feedback.
- **SC-003**: 100% of entered data is preserved when the user navigates away from and back to the wizard step, or when the app is interrupted (e.g., incoming call, backgrounding).
- **SC-004**: All validation errors are displayed inline within 200 milliseconds of the user finishing input on a field, with no silent failures or crashes.
- **SC-005**: Users can complete the full House Dimensions step (all fields filled, valid values, next step reached) in under 2 minutes on first use.
- **SC-006**: The wizard step renders correctly on both phone (compact) and tablet (expanded) form factors without content being clipped or overlapping.
- **SC-007**: Navigation buttons are correctly mirrored in RTL (Arabic) layouts, with no interaction inconsistencies.

## Assumptions

- The multi-step wizard infrastructure (step navigation, progress indicator pattern) is established as part of this feature and will be reused by subsequent wizard steps (#004, #005, #006).
- The 2D preview canvas is a simplified top-down rectangle with ruler markers only — it does not include equipment placement, door/window positioning, or the full interactive blueprint capabilities planned for Feature #008.
- Insulation type options are: None, Polystyrene, Polyurethane, and Mineral Wool. When "None" is selected, thickness is not applicable and the thickness field is hidden.
- Upper bound validation values (length 500m, width 100m, height 15m, ridge height 20m, insulation thickness 500mm) are based on practical limits for commercial poultry house construction and the product specification constraints.
- Data persistence uses the local Room database established in Features #001 and #002. House dimensions are stored as a sub-entity of the Project entity.
- The "Step 1/6" indicator assumes a 6-step wizard as described in the product specification. If the total step count changes, the indicator text updates accordingly.
- Default values are not pre-filled in dimension fields — the user starts with empty fields to avoid assumption-based errors in structural calculations.
