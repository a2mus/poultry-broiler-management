# Screen Flows & UI Structure

## Navigation Map

```
App Launch
│
├── Home / Project List
│   ├── [+ New Project]
│   │   ├── Select Type: "New Installation" | "Existing Assessment"
│   │   ├── Location Setup (GPS auto-detect / manual region select)
│   │   └── → Project Wizard
│   │
│   ├── [Existing Project Card] → Project Dashboard
│   └── [Settings gear icon]
│
├── Project Wizard (step-by-step)
│   ├── Step 1: House Dimensions
│   ├── Step 2: Breed Selection
│   ├── Step 3: Capacity Planning
│   ├── Step 4: Ventilation Design
│   ├── Step 5: Equipment Selection
│   ├── Step 6: Review & Generate
│   └── → Project Dashboard
│
├── Project Dashboard (bottom tabs)
│   ├── Tab: Blueprint
│   ├── Tab: Financial
│   ├── Tab: Risk
│   ├── Tab: Enhancements (existing only)
│   └── Tab: Reports
│
├── Equipment Catalog
│   ├── Browse by Category
│   ├── Search
│   └── Item Detail / Edit
│
├── Breed Catalog
│   ├── Browse Breeds
│   └── Breed Detail / Compare
│
└── Settings
    ├── Language
    ├── Cloud Sync
    └── Account
```

## Screen Specifications

### 1. Home / Project List

**Purpose:** Entry point. Shows all saved projects.

**Layout:**
- App bar: App name + settings icon
- Search/filter bar
- Scrollable list of project cards
- FAB (Floating Action Button): "+ New Project"

**Project Card shows:**
- Project name
- Type badge (New / Existing)
- Location / region
- Date created
- Status indicator (Draft / In Progress / Completed)
- Thumbnail of blueprint (if generated)

**Actions:**
- Tap card → Open Project Dashboard
- Long press → Delete / Duplicate / Share
- FAB → Create New Project dialog

---

### 2. New Project Dialog

**Purpose:** Choose project type and set location.

**Flow:**
1. Select type: "New Installation" or "Existing Assessment"
2. Enter project name
3. Location setup:
   - "Detect my location" button (GPS)
   - Manual: Select country → region → or enter coordinates
   - Climate zone is auto-determined from location
4. "Start" button → Opens Project Wizard

---

### 3. Project Wizard

**Purpose:** Step-by-step project configuration. Progress indicator at top.

#### Step 1: House Dimensions
- Length input (meters)
- Width input (meters)
- Wall height input (meters)
- Orientation selector (N-S / E-W / Custom with compass)
- Roof type selector (Flat / Pitched / Arched)
- Wall material selector
- Floor type selector
- Insulation type + thickness
- For EXISTING: also capture current condition (Good / Fair / Poor)
- Live area calculation display: "Total area: X sq meters"

#### Step 2: Breed Selection
- List of breeds from BreedCatalog
- Each item shows: name, target weight, growth days, space requirement
- "Compare" button to compare 2-3 breeds side by side
- Selected breed highlights its requirements
- Info card: "This breed requires X sq meters per bird, temp range Y-Z C"

#### Step 3: Capacity Planning
- Auto-calculated: "Based on your house (X sqm) and breed (Y sqm/bird), max capacity: Z birds"
- User can override with desired capacity
- Warning if over/under recommended density
- Density indicator: birds/sqm with color (green/yellow/red)
- EU welfare compliance indicator

#### Step 4: Ventilation Design
- Climate zone summary card (auto-detected temps, humidity)
- Recommended ventilation type (based on climate + house size)
- Fan configuration:
  - Number of fans
  - Fan size selector
  - Placement editor (simple grid showing house outline)
- Inlet configuration:
  - Number of inlets
  - Type selector
  - Placement
- Cooling system:
  - Cooling pad toggle
  - Pad area input
- Heating system:
  - Heater toggle
  - Type and count
- For EXISTING: capture current config, system highlights deficiencies
- Airflow calculation display: "Min ventilation: X CFM, Max: Y CFM, Required: Z CFM"

#### Step 5: Equipment Selection
- Category tabs: Feeding | Watering | Heating | Cooling | Lighting | Other
- Equipment list from catalog (searchable)
- For each selected item: quantity input, price (pre-filled, editable)
- Running total at bottom
- "Add custom equipment" option
- For EXISTING: mark existing equipment vs. needed additions

#### Step 6: Review & Generate
- Summary of all inputs across steps
- Edit buttons to go back to any step
- "Generate Blueprint" button
- "Calculate Financials" button
- "Assess Risk" button
- "Generate Full Report" button
- Progress indicators for each generation task

---

### 4. Project Dashboard

**Purpose:** View and interact with project results. Uses bottom tab navigation.

#### Tab: Blueprint
- Interactive 2D view of the house (Compose Canvas)
  - House outline with dimensions
  - Fan placements (icons with airflow direction)
  - Inlet placements
  - Equipment placements (feeding lines, water lines, heaters)
  - Cooling pad areas
  - Door/entry positions
- Touch gestures: pinch zoom, pan, rotate
- Edit mode toggle: tap to move/adjust placements
- "Export PDF" button in top bar
- Dimension annotations on the drawing
- Legend showing icon meanings

#### Tab: Financial
- **Cost Breakdown Card:**
  - Construction costs (pie chart)
  - Equipment costs (list with amounts)
  - Total investment (highlighted)
- **Operating Costs Card:**
  - Electricity per cycle
  - Feed per cycle
  - Labor per cycle
  - Total per cycle
- **Revenue Projection Card:**
  - Revenue per cycle
  - Profit per cycle
  - Cost per bird vs. revenue per bird
- **ROI Card:**
  - ROI timeline graph
  - Months to break even (highlighted number)
  - Annual projected profit
- **Cycles per Year:** configurable slider

#### Tab: Risk Assessment
- **Overall Risk Gauge:** visual meter (green/yellow/orange/red)
- **Mortality Risk Card:**
  - Score (0-100 with color)
  - Factor list with severity badges
  - Recommendations for each factor
- **Welfare Compliance Card:**
  - Score (0-100)
  - Compliance checklist (pass/fail per standard)
  - Gap descriptions with fix recommendations
- **Environmental Risk Card:**
  - Score (0-100)
  - Heat stress risk
  - Cold stress risk
  - Ventilation adequacy indicator
  - Humidity risk

#### Tab: Enhancements (Existing Installations Only)
- Sorted by priority (Critical → High → Medium → Low)
- Each enhancement card:
  - Title + category badge
  - Description
  - Estimated cost
  - Expected benefit
  - Risk reduction indicator
- **Before/After Comparison:**
  - Toggle to show current vs. enhanced state
  - Side-by-side risk scores
  - Side-by-side cost projections
- "Total enhancement cost" summary at bottom
- "Select enhancements" checkboxes to build a custom plan

#### Tab: Reports
- Report type selector: Full | Blueprint Only | Financial Only | Risk Only
- "Generate Report" button
- Report preview (rendered in-app)
- "Export PDF" button
- "Share" button (cloud sync required)
- Report history list

---

### 5. Equipment Catalog (Standalone)

**Purpose:** Browse, search, and manage equipment catalog.

- Category navigation (sidebar or top tabs)
- Search bar
- Equipment list with: name, category, default price, specs summary
- Tap → detail view with full specifications
- "Edit Price" for existing items
- "Add Custom Item" FAB
- Filter by manufacturer

---

### 6. Breed Catalog (Standalone)

**Purpose:** Browse and compare broiler breeds.

- Breed list with: name, target weight, growth days, FCR
- Tap → detail view with all parameters
- "Compare" mode: select 2-3 breeds, see side-by-side table
- Parameters compared: space, weight, days, FCR, temp range, mortality rate

---

### 7. Settings

- **Language:** French / Arabic (with restart prompt)
- **Units:** Metric (fixed for v1)
- **Cloud Sync:** Toggle + sign in
- **Account:** Display name, email (if signed in)
- **Data Management:** Export/import local data
- **About:** App version, licenses, contact
