# UI Specification: Poultry Broiler House Design & Management

This document establishes the user interface guidelines, visual design system, navigation schemas, and interactive component specifications for the Poultry Broiler House Design & Management Android application.

---

## 1. Design System & Design Tokens

To ensure maximum visual excellence and a premium experience, the application adheres to a strict design token system. These values must be implemented directly in the Compose `Theme.kt` definitions.

### 1.1 Color Palettes
The application uses a glare-reducing, high-contrast, harmonious color palette tailored for outdoor field environments. Generic colors are avoided. All primary colors use tailored HSL offsets.

#### Light Mode Theme (Field Glare Reduction)
- **Primary (Forest Teal)**: `#00685c`
- **On Primary**: `#ffffff`
- **Primary Container**: `#1e8275`
- **On Primary Container**: `#f2fffb`
- **Secondary (Warm Ochre)**: `#865300`
- **Secondary Container (Sunset Gold)**: `#fcae49`
- **On Secondary Container**: `#6e4300`
- **Background (Soft Pearl)**: `#f7f9ff`
- **Surface**: `#f7f9ff`
- **On Surface**: `#131d24`
- **Surface Variant**: `#dae3ee`
- **On Surface Variant**: `#3e4946`
- **Success (Emerald Green)**: `#229954`
- **Warning (Amber Gold)**: `#D4AC0D`
- **Danger/Error (Terracotta Red)**: `#CB4335`

#### Dark Mode Theme (Sleek Carbon)
- **Primary (Vibrant Mint)**: `#2ECC71`
- **On Primary**: `#0E141B`
- **Secondary (Sunset Gold)**: `#F39C12`
- **Background (Deep Obsidian)**: `#0C1013`
- **Surface (Charcoal Carbon)**: `#171F26`
- **On Surface**: `#EDEFF1`
- **Surface Variant**: `#242F3A`
- **Success (Neon Emerald)**: `#27AE60`
- **Warning (Bright Sun)**: `#F1C40F`
- **Danger/Error (Neon Crimson)**: `#E74C3C`

### 1.2 Typography
We use **Outfit** for display/headers (modern, geometric, high structural integrity) and **Inter** for body text and numeric data tables (highly legible at small scales).

| Token Name | Font Family | Size (sp) | Weight | Line Height (sp) | Purpose |
|---|---|---|---|---|---|
| `DisplayLarge` | Outfit | 32 | Bold | 40 | Screen Titles (Hero) |
| `TitleMedium` | Outfit | 20 | SemiBold | 26 | Card Headers, Dialog Titles |
| `BodyLarge` | Inter | 16 | Regular | 22 | General reading, descriptions |
| `BodyMedium` | Inter | 14 | Regular | 20 | Subtexts, form labels |
| `NumericData` | Inter | 15 | Medium (Monospace) | 18 | Dimensions, calculations, tabular figures |
| `LabelSmall` | Inter | 12 | Bold | 16 | Status badges, category labels |

### 1.3 Spacing & Grid System
The app uses an **8dp baseline grid** for margins, padding, and alignments.
- **Micro-spacing**: `4dp` (spacing between text/icon)
- **Small spacing**: `8dp` (internal component padding)
- **Medium spacing**: `16dp` (standard card padding, element spacing)
- **Large spacing**: `24dp` (outer screen margins, page-level separation)
- **Grid Layout**: 4-column layout for Phone screens, 8-column adaptive grid layout for Tablet screens.

### 1.4 Shapes & Elevation
- **Card Corners**: Rounded `16dp` (Material 3 Card)
- **Button Corners**: Rounded `24dp` (pill-shaped)
- **Badge Corners**: Rounded `8dp`
- **Dialog Corners**: Rounded `28dp`
- **Elevation levels**:
  - `Level 0`: Flat (0dp)
  - `Level 1`: Low elevation card (2dp)
  - `Level 2`: Dynamic float / hover state card (6dp)
  - `Level 3`: Bottom Sheet / Modals (12dp)

---

## 2. Global UI Component Standards

To maintain visual consistency across all screens, standard UI components are defined below.

### 2.1 Project Cards
Used on the Home Screen to display individual projects.
- **Structure**:
  - Top line: Project Name (TitleMedium) + Type Badge (`NEW` / `EXISTING`)
  - Sub-header: Location/Region + Creation Date (BodyMedium)
  - Left-hand thumbnail: Auto-generated 2D thumbnail preview of house dimensions.
  - Right-hand side: Circular welfare/compliance indicator score (0-100) or status badge.
- **Micro-interaction**: Tap card has a ripple effect (`RippleTheme`), scale animates down to `98%` on long-press before menu shows.

### 2.2 Status & Compliance Badges
Badges use high-contrast text on low-saturation background colors to avoid visual pollution.

| Badge Type | Color Palette (Dark Theme / Light Theme) | Text Label |
|---|---|---|
| **Draft** | Gray Background (`Surface Variant` / Text `On Surface`) | DRAFT / BROUILLON |
| **Completed** | Slate Teal (`Primary` / Text `On Primary`) | COMPLETED / TERMINÉ |
| **Welfare Pass** | Emerald/Green Background / Dark Green Text | EU COMPLIANT / CONFORME UE |
| **Welfare Fail** | Red Background / Dark Red Text | NON-COMPLIANT / NON-CONFORME |
| **High Priority**| Terracotta Red / White Text | CRITICAL / CRITIQUE |

### 2.3 Form & Input Controls
- **Insulated Wall / Roof Selector**: Segmented Button control allowing immediate selection with visual icons representing Prefab, Steel, or Block construction.
- **Numeric Fields (Dimensions)**: Always feature a persistent trailing unit label (e.g. `m`, `mm`, `CFM`, `sqm`) aligned to the right. Text fields automatically trigger numeric keypads.
- **Capacity Override Slider**: Interactive slider with track coloring that changes from Green to Yellow/Red as density approaches or exceeds welfare thresholds.

### 2.4 Bottom Sheets
- Used for selection menus, custom equipment entries, and comparisons.
- Always include a drag handle icon at the top center.
- Swipe-to-dismiss support with speed-based snap thresholds.

---

## 3. Navigation & Screen Architecture Map

The application follows a single-activity architecture using **Compose Navigation**.

```
[MainHost]
   ├── Route: HOME ("projects_list")
   │      └── Dialog: NEW_PROJECT_WIZARD_INIT ("new_project_dialog")
   │
   ├── Route: WIZARD ("project_wizard/{projectId}")
   │      ├── Step 1: House Dimensions
   │      ├── Step 2: Breed Selection
   │      ├── Step 3: Capacity Planning
   │      ├── Step 4: Ventilation Design
   │      ├── Step 5: Equipment Selection
   │      └── Step 6: Review & Generate
   │
   ├── Route: DASHBOARD ("project_dashboard/{projectId}")
   │      ├── Bottom Tab 1: Blueprint
   │      ├── Bottom Tab 2: Financials
   │      ├── Bottom Tab 3: Risk Assessment
   │      ├── Bottom Tab 4: Enhancements (Existing Only)
   │      └── Bottom Tab 5: Reports
   │
   ├── Route: CATALOG_EQUIPMENT ("equipment_catalog")
   ├── Route: CATALOG_BREEDS ("breeds_catalog")
   └── Route: SETTINGS ("settings")
```

---

## 4. Detailed Screen Specifications

The application UI consists of 7 core functional views, fully localized and visualised in the Stitch project as 14 distinct screens (French and Arabic). The visual layout structures are taken directly from the Stitch templates, while the underlying data parameters and constraints are defined by the business requirements.

### 4.1 Home Screen / Dashboard (Tableau de bord / لوحة القيادة)
- **Stitch Screen IDs**:
  - French: `c71e8af9b25545368b560d3911abbffd` (Accueil - Gestionnaire Avicole)
  - Arabic: `b9ff9354812042b8a8d8fb39d674b8a5` (الرئيسية - مدير الدواجن)
- **Top App Bar**:
  - Header: Forest Teal text "AgriManage Pro" with Hamburger menu icon (`menu`) on the leading edge (left in LTR French, right in RTL Arabic).
  - Action/Status: Profile photo thumbnail of agricultural manager in technical attire, plus Settings gear icon (`settings`) on desktop layout.
  - Search Bar: Docked under App Bar (inline for tablet/desktop, collapsible for mobile).
    - French Placeholder: "Rechercher un projet..." or "Rechercher des bâtiments..."
    - Arabic Placeholder: "البحث عن المباني..." or "بحث عن المشاريع..."
- **Visual Layout (Bento Grid Style)**:
  - **Active Projects Section** (Projets Actifs / المشاريع النشطة):
    - Lists active broiler house projects using a responsive card layout.
    - Top card image displays professional industrial poultry house photography corresponding to the farm zone.
    - Status Badge:
      - Completed: French `TERMINÉ` (Forest Teal container) / Arabic `مكتمل` (RTL mirrored).
      - Draft: French `BROUILLON` (Gray Outline) / Arabic `مسودة`.
      - Pending: French `EN ATTENTE` / Arabic `في الانتظار` (Ochre Yellow).
    - Metadata rows:
      - Location with map indicator pin icon (`location_on`): e.g. "Sétif, Algérie" / "Batna, Algérie".
      - Capacity count label: e.g. "Capacité: 25,000" or "18,500" units.
      - Climate/Ventilation state: e.g. "Ventilation: OPTIMAL".
      - Compliance Score: Large numeric display (e.g. `98%` or `64%`) representing the welfare scorecard indicator.
  - **Performance Analysis Beta Card** (Analyse de Performance):
    - Solid Forest Teal primary container with white text.
    - Description text detailing growth reports, feed conversion ratios (FCR), and consumption indices.
    - Prominent action button: "Ouvrir les Rapports" / "فتح التقارير".
  - **New Project Placeholder Card**:
    - Dashed border card with a prominent "+" icon (`add`) triggering the New Installation/Wizard dialog.
- **Navigation Options**:
  - Bottom navigation bar (Mobile-only, mirrored for RTL):
    - Tab 1: Projects (`list_alt` / المشاريع)
    - Tab 2: Design (`architecture` / التصميم)
    - Tab 3: Reports (`finance_mode` / التقارير)
    - Tab 4: Health (`health_and_safety` / الصحة)
  - Floating Action Button (FAB): Pill/rounded container displaying `architecture` or `add` to launch project design.

### 4.2 House Dimensions Wizard Step (Dimensions du Bâtiment / أبعاد المبنى)
- **Stitch Screen IDs**:
  - French: `577985ee20134c9c98b609137067e6ef` (Dimensions du Bâtiment)
  - Arabic: `5bcbb9baf5774205800d7b2813c37abd` (أبعاد المبنى)
- **Header & Progress**:
  - Shows progress indicator badge (e.g. "Étape 1/4" / "الخطوة 1 من 4").
  - Title: French "Dimensions du Bâtiment" / Arabic "أبعاد المبنى".
  - Subtext: French "Veuillez saisir les mesures structurelles..." / Arabic "أدخل القياسات الدقيقة لضمان حسابات تهوية وتدفئة صحيحة.".
- **Visual Layout (Bento Grid Input Cards)**:
  - **Structural Measures Card** (Mesures Structurelles / القياسات الأساسية):
    - Form fields for Length (Longueur / الطول), Width (Largeur / العرض), and Wall Height (Hauteur des murs / ارتفاع الجدار).
    - Persistent trailing unit label "m" aligned to the right inside the input field.
    - Live calculated field: "Surface Totale Estimée" / "المساحة التقديرية" in m² (e.g. `2,400 m²` or `1,200.0 m²`).
  - **Wall Insulation Card** (Wall Insulation / عزل الجدران):
    - Radio selectors with illustrative icon badges:
      - Prefab (Préfabriqué / عزل مسبق الصنع) with `house` outline icon.
      - Block (Bloc / طوب بلوك) with `view_module` brick icon.
      - Steel (Acier / فولاذ) with `architecture` structural frame icon.
  - **Roof Type Card** (Type de Toit / نوع السقف):
    - Segmented checkmark cards:
      - Gable (À Pignon / جملون) with triangle icon (`change_history` or `roofing`).
      - Flat (Plat / مسطح) with flat line icon (`horizontal_rule` or `rectangle`).
  - **2D Preview Canvas Box**:
    - Aspect-square box showing a real-time scaled top-down outline of the building dimensions with ruler markers.
- **Wizard Navigation Action Bar**:
  - Bottom bar with "Previous" (Précédent / السابق) and "Next" (Suivant / التالي) action buttons. Mirrors direction for RTL Arabic layout.

### 4.3 Blueprint View Dashboard Tab (Plan du Bâtiment / مخطط المبنى)
- **Stitch Screen IDs**:
  - French: `1f34cc21385644d4baa9cb8b79c63356` (Plan du Bâtiment)
  - Arabic: `68fb51a2c9fb464ca260ab38151fa9e3` (مخطط المبنى)
- **Header & Controls**:
  - Title: French "Blueprint" / Arabic "مخطط المبنى".
  - Subtitle: Displaying current dimensions (e.g., "Bâtiment A1 : 120.0m x 10.0m").
  - Quick action controls: Zoom In, Zoom Out, Recenter, Edit.
- **Interactive 2D Blueprint Canvas**:
  - Detailed grid background (light grey lines).
  - Scaled structural outline representing the broiler house.
  - Vectorized placement symbols:
    - Exhaust Fans: `cyclone` / `mode_fan` propeller icon. Placed along the wall end (Tunnel ventilation).
    - Heaters: `mode_fan_off` / `local_fire_department` flame icon. Placed in a distributed grid.
    - Feeding Lines: Gold / yellow solid horizontal line.
    - Watering Lines: Light blue dashed/solid horizontal line.
    - Cooling Pads: Green/blue dashed wave rectangle at the air intake end.
  - Interactive handles on selected components (blue anchor ring overlay).
- **Legend Panel**:
  - Explanatory panel for symbols:
    - Exhaust Fan (Ventilateur d'extraction / مراوح الشفط)
    - Heater (Chauffage / دفايات)
    - Feed/Water Lines (Alimentation/Eau / خطوط العلف والماء)
  - Scale/precision tolerance indicator (e.g. `± 0.05m`).
- **Footer Status Details**:
  - Maximum capacity constraint value, current density status (birds/m²), and last updated timestamp.

### 4.4 Risk Assessment Dashboard Tab (Évaluation des Risques / تقييم المخاطر)
- **Stitch Screen IDs**:
  - French: `a6d82ba1d16b4680a35d57e204549926` (Évaluation des Risques)
  - Arabic: `19cc2b50668441aca87d803d50757307` (تقييم المخاطر)
- **Global Risk Gauge**:
  - Radial SVG gauge illustrating risk level (Low, Medium/Moyen, High/Élevé, Critical).
  - Current status score (e.g. `54/100`).
  - Contextual warning bar (e.g., alert on temperature, humidity, or over-density).
- **Mortality Risk Breakdown**:
  - Categorized stress factors (Stress Thermique, Infection Virale, Qualité de l'Eau) with severity badges (Modéré, Faible, Critique).
- **Environmental Factors Grid**:
  - Bento grid cards for real-time sensor metrics:
    - Temperature (Température / درجة الحرارة) in °C.
    - Humidity (Humidité / الرطوبة) in %.
    - Air Quality (Qualité de l'Air / جودة الهواء) in AQI.
  - Active pulsing status indicator color dot (Green/Yellow/Red).
- **EU Welfare Compliance Checklist**:
  - Multi-checkbox checklist card representing official guidelines.
  - Compliance percentage indicator badge (e.g., "85% Conforme").
  - Mandatory checks:
    - Biosecurity Entry Control (Contrôle de la Biosécurité des Entrées / مراقبة الأمن الحيوي)
    - Medication/Vaccine Registry (Registre de Médication / سجل الأدوية واللقاحات)
    - Stocking Density Standards (Normes d'Espace et Densité / معايير الكثافة والمساحة) - flags alerts if density exceeds rules.
    - Emergency Ventilation Backup (Systèmes de Ventilation de Secours / أنظمة التهوية الاحتياطية)

### 4.5 Financial Analysis Dashboard Tab (Analyse Financière / البيانات المالية)
- **Stitch Screen IDs**:
  - French: `94a19a0fe76c4421bb15ccb480073a2e` (Analyse Financière)
  - Arabic: `8f609b3db1144a8c88502ace302203ca` (البيانات المالية)
- **CapEx & OpEx Summary Bento Grid**:
  - **Capital Expenditure (CapEx)**: Total infrastructure and design cost (e.g., `4,250,000 DZD`) with progress bar showing budget allocation (e.g., 75% infrastructure).
  - **Operating Expenditure (OpEx)**: Ongoing costs per flock cycle (e.g., `840,000 DZD`) with trend indicators.
- **Operating Costs Breakdown Cards**:
  - **Feed (Alimentation / الأعلاف)**: Cost per unit and total volume details.
  - **Electricity (Électricité / الكهرباء)**: Consumption rates in kWh and industrial tariff tier (G1).
  - **Labor (Main-d'œuvre / العمالة)**: Staff headcount and hours per cycle.
- **ROI Projection Chart**:
  - Scaled area chart tracing cost payback periods (break-even point marker).
- **Real-Time Simulation Control**:
  - Range slider to adjust expected flock cycles per year (from 4 to 7).
  - Dynamic calculations updating the *Estimated Annual Profit* (Profit Annuel Estimé / الربح السنوي التقديري).

### 4.6 Technical Improvements / Enhancements Dashboard Tab (Améliorations Techniques / تحسينات - مبنى ألفا)
- **Stitch Screen IDs**:
  - French: `643b3f66ad0246aca5c705c18796af60` (Améliorations Techniques)
  - Arabic: `c8bd2acc907d4a19b16b43084bb00968` (تحسينات - مبنى ألفا)
- **Enhancement Recommendation Bento Grid**:
  - Priority list of recommended upgrades, color-coded by urgency:
    - **Critical (Critique / حرج)**: e.g., Ventilation capacity upgrades (12,000 CFM to 28,000 CFM) with projected ROI timeline (e.g., 14 months) and energy efficiency/comfort metrics.
    - **High (Élevé / مرتفع)**: e.g., Wall insulation updates (R-12 to R-28 using polyurethane foam) with ROI and heat loss reduction percentage.
    - **Medium (Moyen / متوسط)**: e.g., LED Smart lighting upgrades from fluorescent T8 tubes.
  - Layout features side-by-side comparison boxes showing **Current State vs. Proposed State** (État Actuel vs. État Proposé / الوضع الحالي مقابل الوضع المقترح).
- **Project Plan Action Bar**:
  - Checkboxes/buttons to add/remove enhancements from the custom investment plan.
  - Summary totals (Total Estimated Cost vs. Annual Savings) at the bottom.

### 4.7 Performance Reports Dashboard Tab (Rapports de Performance / تقارير - مبنى ألفا)
- **Stitch Screen IDs**:
  - French: `4580a1f0f56b4471a17f76886d7f673b` (Rapports de Performance)
  - Arabic: `5178c6f00ab2474e8434a438c4973c4e` (تقارير - مبنى ألفا)
- **Overall Assessment Summary**:
  - Title: French "Résumé de l'Évaluation" / Arabic "تقارير - مبنى ألفا".
  - Actions: PDF Export (`picture_as_pdf`) and Link Sharing (`share`).
- **Key Metrics Grid**:
  - Compliance Score (Score de Conformité / نسبة المطابقة): e.g. `94/100` with progress indicator.
  - Total Capacity (Capacité Totale / السعة الإجمالية): e.g. `12,450 units`.
  - Ventilation Output (Ventilation / التهوية): e.g. `420k m³/h`.
  - Total Estimated Cost (Coût Total Estimé / التكلفة الإجمالية): e.g. `84.2k€` or in local currency.
- **Interactive Blueprint Preview**:
  - Display container showcasing the 2D layout overlay with real-time zone temperature/humidity details.
  - Action link to switch to detailed plan editor.

### 4.8 Other Project Wizard Steps (Reference Data Flows)
To support the complete design capability, the following wizard screens are integrated into the primary navigation flow:
- **Step 2: Breed Selection**: Select Cobb 500 or Ross 308. Standard growth parameters (Growth Days, Feed Conversion Ratio (FCR), comfort thresholds) are pre-loaded from the SQLite catalog.
- **Step 3: Capacity Planning**: Live-calculate capacity based on HSL color-coded density warning bar:
  - Green (<= 33 kg/m² or EU welfare standard density).
  - Yellow (> 33 to <= 39 kg/m²: "Requires advanced cooling/ventilation").
  - Red (> 39 kg/m²: "Exceeds welfare safety margins").
- **Step 4: Ventilation Design**: Set configuration parameters for exhaust fans, side-wall inlets, cooling pads, and heating units. Calculations compare total system output against required target CFM.
- **Step 5: Equipment Selection**: Add watering cups, feeder lines, lighting fixtures, and custom accessories to the bill of materials (BOM).
- **Step 6: Review & Generate**: Group all parameters into a comprehensive design summary card.

---

## 5. Interactive 2D Canvas Specification

The Blueprint layout uses Jetpack Compose `Canvas` to draw structural layouts to scale.

### 5.1 Coordinate System & Scaling
- The canvas viewport automatically scales the house boundaries to fill `85%` of the available canvas width/height.
- Scale conversions are marked: `1 meter = X pixels` based on dynamic canvas sizing.
- Dimension labels are placed outside the walls using small dashed extension lines.

### 5.2 Element Icons & Colors
Symbols are vectorized to ensure pixel-perfect clarity, matching the Material Symbols used in Stitch.

| Element | Icon Symbol | Color Coding (Light / Dark Theme) | Description |
|---|---|---|---|
| **Exhaust Fan** | `mode_fan` / `cyclone` | Dark Gray (`#2C3E50`) / Teal Mint (`#2ECC71`) | Tunnel ventilation fans placed on walls |
| **Inlet** | `open_in_full` / Rectangle | Dark Teal (`#16A085`) / Mint (`#58D68D`) | Passive air inlets along length |
| **Cooling Pad** | Wave / Grid | Blue (`#2980B9`) / Cyan (`#3498DB`) | Evaporative cooling pad at intake end |
| **Heater** | `local_fire_department` | Red (`#C0392B`) / Orange (`#E67E22`) | Central heating units suspended |
| **Feeding Line** | Solid Thin Line | Gold (`#F39C12`) / Sun Yellow (`#F1C40F`) | Linear pan feeding line |
| **Watering Line**| Dashed Thin Line | Blue (`#2980B9`) / Cyan (`#3498DB`) | Nipple drinker water line |

### 5.3 Touch Gestures & Edit Mode
- **Navigation (Default Mode)**:
  - Pinch-to-zoom (scales canvas from `0.5x` to `4.0x`).
  - Two-finger drag (pans the viewport across the scaled blueprint).
- **Edit Mode (Toggle)**:
  - Tapping an element highlights it with an emerald blue selection ring (`#3498DB` or `#2ECC71`) and shows anchor drag handles.
  - Dragging moves elements along the inner boundary of the house walls (fans snap to outer walls, heaters snap to ceiling grid coordinates).
  - Double-tap deletes an element.

---

## 6. Accessibility & Localization

The application target is full usability for global agricultural agents working in diverse settings.

### 6.1 Accessibility Standards
- **Touch Target Sizing**: All buttons, counters, and tabs have a minimum click-target size of `48dp` x `48dp` to allow easy usage with gloves or dirty hands in field conditions.
- **Contrast Ratios**: Body text meets a minimum contrast ratio of `4.5:1` (WCAG AA). Numerical critical warnings meet `7:1` (WCAG AAA).
- **TalkBack & Accessibility Labels**: Every icon and interactive element has mandatory descriptive `contentDescription` attributes in localized resource strings.

### 6.2 Localization & RTL Support
- **RTL Layout (Arabic)**:
  - Enabled via `dir="rtl"` in HTML and native RTL layout configurations (`android:supportsRtl="true"`).
  - Navigation drawer and top app bar icons mirror (Hamburger menu and close buttons are positioned on the right).
  - Bottom navigation bar tabs read from right-to-left.
  - Next/Back wizard buttons are flipped (Next button on the left pointing left, Cancel/Back on the right).
- **Bilingual Assets**: All strings are stored in `res/values-fr/strings.xml` (French, primary target) and `res/values-ar/strings.xml` (Arabic). Text fields adapt spacing to prevent word-wrapping overlap.
