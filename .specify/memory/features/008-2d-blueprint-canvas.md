# Feature Brief: 2D Interactive Blueprint Canvas

**Sequence**: #008 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: XL

## Dependencies

- **Requires**: #003 House Dimensions Wizard, #006 Ventilation & Climate Design, #007 Equipment Selection & Catalog
- **Unlocks**: #011 Enhancement & Upgrade Planning, #012 PDF Export & Reporting
- **Parallelizable with**: #009 Financial Dashboard, #010 Risk & Welfare Assessment (partially — these share dependencies but not each other)

## Feature Description

The 2D Interactive Blueprint Canvas is the flagship visual feature of the application and the primary reason consultants choose this tool over spreadsheets. It renders a scaled, top-down schematic of the broiler house layout, showing structural boundaries, dimensions, annotations, and the precise placement of all ventilation and equipment components. This is the deliverable that consultants present to farmers during on-site meetings.

The canvas draws the house outline to scale based on dimensions from Wizard Step 1, automatically fitting the building to 85% of the available viewport. Dimension labels are placed outside the walls using dashed extension lines, and a scale indicator (e.g., "1:100") shows the mapping between real-world meters and screen pixels. Inside the house, vectorized symbols represent equipment: exhaust fans (propeller icons on tunnel walls), heaters (flame icons in distributed grid), feeding lines (gold horizontal lines), watering lines (blue dashed lines), cooling pads (wave rectangles at intake end), and air inlets (along wall lengths). Each symbol follows the color-coding and icon system defined in the UI spec.

In the default navigation mode, the canvas supports pinch-to-zoom (0.5x to 4.0x scale range) and two-finger drag for panning — essential for detailed inspection on phone screens. A toggle activates Edit Mode, where tapping an element highlights it with a selection ring and anchor handles. Dragging moves elements within the house boundaries, with smart snapping: fans snap to outer walls, heaters snap to ceiling grid coordinates, feeding/watering lines snap to horizontal rows. Double-tap deletes an element. These interactive placements are the "Should Have" enhancement from the MoSCoW table that elevates the canvas from a static render to a design tool.

The canvas is displayed as the first tab (Blueprint) on the Project Dashboard, accessible after completing the wizard or from any existing project. A legend panel explains all symbols, and footer status details show maximum capacity, current density, and the last updated timestamp. The canvas must render layout changes under 16ms to maintain 60 FPS per the non-functional requirements.

## Derived From

- **Product Spec**: §5.1 Core Feature 7 — 2D Interactive Canvas (scaled top-down schematic, structural boundaries, annotations, dimensions, equipment placements, pinch-to-zoom, panning, tap-and-drag editing)
- **Product Spec**: §5.3 MoSCoW — 2D Blueprint Rendering (Must Have), Interactive 2D Placement Dragging (Should Have)
- **UI Spec**: §4.3 Blueprint View Dashboard Tab (Stitch screens: `1f34cc21`, `68fb51a2`), §5 Interactive 2D Canvas Specification (§5.1 Coordinate System, §5.2 Element Icons, §5.3 Touch Gestures)
- **Constitution**: Article 1.3 (Compose Canvas API for 2D drawing), Article 3.4 (Responsive — prioritize 10-inch tablet, phone supports zoom/pan)

## Acceptance Criteria Summary

- [ ] Canvas renders a to-scale top-down house outline based on project dimensions with dimension labels and scale indicator
- [ ] All ventilation and equipment components display as vectorized symbols with correct color-coding per ui-spec §5.2
- [ ] Pinch-to-zoom (0.5x–4.0x) and two-finger panning work smoothly in navigation mode
- [ ] Edit mode toggle enables tap-to-select (selection ring + handles), drag-to-move (with wall/grid snapping), and double-tap-to-delete
- [ ] Legend panel displays explanations for all symbols; footer shows capacity, density, and timestamp
- [ ] Canvas redraw completes under 16ms (60 FPS) on mid-range Android devices
- [ ] Blueprint tab is the primary view on the Project Dashboard

## UI Components (if applicable)

- **BlueprintCanvas**: Main Compose Canvas composable with coordinate system, scaling, and element rendering
- **CanvasToolbar**: Zoom In, Zoom Out, Recenter, and Edit Mode toggle buttons per ui-spec §4.3
- **SelectionRing**: Blue anchor overlay displayed on selected elements in edit mode
- **BlueprintLegend**: Explanatory panel for fan, heater, feed/water line, cooling pad, and inlet symbols
- **BlueprintFooter**: Status bar showing max capacity, current density (birds/m²), and last updated timestamp

## Technical Hints

- Constitution Article 1.3 mandates Compose Canvas API — no external drawing libraries
- Performance target of 16ms redraw requires efficient canvas operations: minimize allocations during `onDraw`, cache Paint objects, use `clipRect` to skip off-viewport elements
- Constitution Article 3.4 requires the canvas to prioritize 10-inch tablet layout; phone screens must support zoom/pan for usability
- Element placement coordinates should be stored in the Room database as part of the project data model, relative to the house origin (0,0 = bottom-left corner)

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
