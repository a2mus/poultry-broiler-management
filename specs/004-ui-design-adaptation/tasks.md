# Tasks: UI Design Adaptation

**Input**: Design documents from `/specs/004-ui-design-adaptation/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic asset configuration

- [x] T001 Configure font resources in app/src/main/res/font/ for Outfit and Inter
- [x] T002 [P] Configure localization resources in app/src/main/res/values-fr/strings.xml and app/src/main/res/values-ar/strings.xml for UI keys

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core UI styling tokens and theme setup that MUST be complete before ANY user story can be implemented

- [x] T003 Update color tokens in app/src/main/java/com/poultry/broiler/presentation/theme/Color.kt with HSL colors
- [x] T004 [P] Update typography configurations in app/src/main/java/com/poultry/broiler/presentation/theme/Type.kt
- [x] T005 [P] Update shapes configuration in app/src/main/java/com/poultry/broiler/presentation/theme/Shape.kt
- [x] T006 [P] Update spacing configurations in app/src/main/java/com/poultry/broiler/presentation/theme/Spacing.kt
- [x] T007 [P] Configure HSL theme mapping in app/src/main/java/com/poultry/broiler/presentation/theme/Theme.kt

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - App Theme and Styling Update (Priority: P1) 🎯 MVP

**Goal**: Apply premium design system tokens across base layouts and wizard scaffolding.

**Independent Test**: Verify correct colors/fonts are active in both light and dark mode on base app views.

- [x] T008 [US1] Update common custom component padding and spacing references in app/src/main/java/com/poultry/broiler/presentation/components/
- [x] T009 [US1] Update wizard layout scaffold to use the new Theme.kt color scheme in app/src/main/java/com/poultry/broiler/presentation/wizard/WizardScreen.kt

**Checkpoint**: At this point, the baseline theme and styling are verified.

---

## Phase 4: User Story 2 - Bento Grid Home Screen (Priority: P1)

**Goal**: Redesign the Home dashboard to utilize the responsive Bento Grid layout.

**Independent Test**: Verify that the project dashboard is styled in Bento layout and has the 98% scale long-press animation.

- [x] T010 [P] [US2] Redesign project item card composable in app/src/main/java/com/poultry/broiler/presentation/home/components/ProjectCard.kt
- [x] T011 [P] [US2] Implement active statistics summary banner in app/src/main/java/com/poultry/broiler/presentation/home/components/PerformanceSummaryCard.kt
- [x] T012 [US2] Update home screen layout to Bento Grid in app/src/main/java/com/poultry/broiler/presentation/home/HomeScreen.kt

**Checkpoint**: Bento Grid Home Screen should be fully functional and visually aligned.

---

## Phase 5: User Story 3 - House Dimensions Step with Live Preview (Priority: P1)

**Goal**: Adapt the House Dimensions wizard step with custom selectors and a real-time scaled 2D preview.

**Independent Test**: Enter width and length values, verifying that the floor area updates and the outline canvas resizes under 300ms.

- [ ] T013 [P] [US3] Redesign segmented buttons for wall insulation in app/src/main/java/com/poultry/broiler/presentation/wizard/components/InsulationSelector.kt
- [ ] T014 [P] [US3] Redesign segmented buttons for roof types in app/src/main/java/com/poultry/broiler/presentation/wizard/components/RoofTypeSelector.kt
- [ ] T015 [P] [US3] Redesign live building outline preview in app/src/main/java/com/poultry/broiler/presentation/wizard/components/DimensionPreviewCanvas.kt
- [ ] T016 [US3] Adapt step 1 layout structure to a 2-column bento form in app/src/main/java/com/poultry/broiler/presentation/wizard/HouseDimensionsStep.kt

**Checkpoint**: House Dimensions wizard screen is fully functional and responsive.

---

## Phase 6: User Story 4 - Bilingual Navigation & RTL Mirroring (Priority: P1)

**Goal**: Support French and Arabic locales, with layout flipping (RTL) for Arabic.

**Independent Test**: Switch system language to Arabic and check that the navigation menu, layout orientation, and wizard buttons are mirrored.

- [x] T017 [US4] Implement layout direction wrapper for RTL support in app/src/main/java/com/poultry/broiler/presentation/navigation/MainNavigation.kt
- [x] T018 [P] [US4] Translate and add Arabic string properties in app/src/main/res/values-ar/strings.xml
- [x] T019 [P] [US4] Translate and add French string properties in app/src/main/res/values-fr/strings.xml

**Checkpoint**: App localization and RTL mirroring behave correctly.

---

## Phase 7: User Story 5 - Interactive 2D Blueprint Canvas (Priority: P2)

**Goal**: Adapt the 2D blueprint tab with styled symbols, scale legends, and zoom gestures.

**Independent Test**: Pan and pinch-to-zoom on the blueprint grid to verify smooth scaling (0.5x to 4.0x) and crisp symbol drawing.

- [ ] T020 [P] [US5] Implement exhaust fan and heater vectorized symbols in app/src/main/java/com/poultry/broiler/presentation/design/components/BlueprintSymbols.kt
- [ ] T021 [P] [US5] Update blueprint canvas interaction and scale matrix in app/src/main/java/com/poultry/broiler/presentation/design/components/BlueprintCanvas.kt
- [ ] T022 [US5] Redesign blueprint legend and details footer in app/src/main/java/com/poultry/broiler/presentation/design/BlueprintScreen.kt

**Checkpoint**: The interactive Blueprint tab rendering matches the Stitch specification.

---

## Phase 8: User Story 6 - Risk Assessment and Compliance Checklist (Priority: P2)

**Goal**: Render a radial safety gauge, sensor grid cards, and an interactive EU checklist card.

**Independent Test**: Toggle compliance checkboxes and check that risk percentage and compliance metrics recalculate dynamically.

- [ ] T023 [P] [US6] Implement radial risk progress gauge in app/src/main/java/com/poultry/broiler/presentation/health/components/RiskScoreGauge.kt
- [ ] T024 [P] [US6] Redesign environmental sensor metrics cards in app/src/main/java/com/poultry/broiler/presentation/health/components/SensorGrid.kt
- [ ] T025 [P] [US6] Redesign EU compliance checklist card in app/src/main/java/com/poultry/broiler/presentation/health/components/WelfareChecklist.kt
- [ ] T026 [US6] Update risk screen layout in app/src/main/java/com/poultry/broiler/presentation/health/RiskScreen.kt

**Checkpoint**: The Risk Assessment tab displays environmental telemetry and checks compliance scores.

---

## Phase 9: User Story 7 - Financial Analysis and Enhancements (Priority: P2)

**Goal**: Implement CapEx/OpEx summary cards, ROI chart, flock simulation slider, and side-by-side improvement comparison boxes.

**Independent Test**: Drag flock slider and check that annual profit calculates dynamically. Confirm that improvement cards show ROI details.

- [ ] T027 [P] [US7] Implement CapEx/OpEx summary cards in app/src/main/java/com/poultry/broiler/presentation/reports/components/FinancialSummaryGrid.kt
- [ ] T028 [P] [US7] Implement ROI payback area chart in app/src/main/java/com/poultry/broiler/presentation/reports/components/RoiPaybackChart.kt
- [ ] T029 [P] [US7] Implement flock cycles slider simulator in app/src/main/java/com/poultry/broiler/presentation/reports/components/FlockCyclesSimulator.kt
- [ ] T030 [P] [US7] Implement side-by-side technical improvements comparison cards in app/src/main/java/com/poultry/broiler/presentation/catalog/components/UpgradeRecommendationCard.kt
- [ ] T031 [US7] Update financial analysis screen layout in app/src/main/java/com/poultry/broiler/presentation/reports/FinancialScreen.kt

**Checkpoint**: Financial dashboard metrics and technical upgrades calculations are fully integrated.

---

## Phase 10: Polish & Cross-Cutting Concerns

**Purpose**: Performance, accessibility verification, and end-to-end validation.

- [ ] T032 Performance auditing of Composable recomposition counts
- [ ] T033 Verify accessibility labels and touch target areas are >= 48dp
- [ ] T034 Run quickstart.md validation scenarios

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Can start immediately.
- **Foundational (Phase 2)**: Depends on Phase 1. Blocks all subsequent user stories.
- **User Stories (Phases 3+)**: All depend on Phase 2. Can be worked on in parallel or sequentially.
- **Polish (Phase 10)**: Depends on all user stories being complete.

---

## Parallel Opportunities

- Setup tasks (T001-T002) can run in parallel.
- Foundational styling token configs (T003-T007) can run in parallel.
- Card items and banner components (T010-T011) can run in parallel.
- Wizard form selectors (T013-T015) can run in parallel.
- Layout translation properties (T018-T019) can run in parallel.
- Vectorized canvas symbols (T020-T021) can run in parallel.
- Health sub-components (T023-T025) can run in parallel.
- Financial charts and components (T027-T030) can run in parallel.

---

## Implementation Strategy

### MVP First (User Stories 1-4)
1. Complete Setup and Foundational styling.
2. Adapt App Theme and Bento Home screen.
3. Adapt House Dimensions step 1 and dynamic canvas.
4. Support Arabic translation and RTL mirroring.
5. **STOP and VALIDATE**: Verify core MVP flows before proceeding to P2 dashboard tabs.
