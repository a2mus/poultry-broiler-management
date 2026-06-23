# Feature Brief: PDF Export & Reporting

**Sequence**: #012 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Large

## Dependencies

- **Requires**: #008 2D Blueprint Canvas, #009 Financial Dashboard, #010 Risk & Welfare Assessment
- **Unlocks**: #013 Cloud Sync & Link Sharing
- **Parallelizable with**: #011 Enhancement & Upgrade Planning

## Feature Description

The PDF Export is the primary deliverable that consultants hand off to their farming clients. After completing a broiler house design or assessment, the consultant generates a professional, print-ready PDF document that serves as both a technical specification and a business proposal. This is the tangible output that justifies the consultant's work and drives the farmer's investment decision.

The exported PDF document contains multiple sections assembled from the project's data: a cover page with project name, type, location, and generation date; the 2D blueprint layout rendered to scale with all equipment placements and a legend; a complete equipment bill of materials (BOM) table listing all items, quantities, unit prices, and subtotals; the financial estimates including CapEx, OpEx, per-cycle and annual projections with the ROI payback period; the risk and welfare assessment scorecards with compliance status; and for "Existing Assessment" projects, the enhancement recommendations with Before/After comparisons.

The PDF must be generated entirely offline using the Android `PdfDocument` API — no server-side rendering or third-party cloud services. The canvas layout from the Blueprint feature is re-rendered onto the PDF page at a print-appropriate resolution and scale. Tables and text blocks use styled formatting that mirrors the app's design system (Outfit headers, Inter body text) while being optimized for print legibility.

Generated PDFs are stored in app-private storage (`Context.filesDir`) per the constitution's security requirements. The user can preview the PDF, save it to device storage (via Android share intents), or print it directly. The Reports tab (Dashboard Tab 5) serves as the generation and management interface, showing key metrics in a summary grid and providing the PDF export and sharing actions.

PDF generation must complete in under 3 seconds on standard mid-range mobile devices, per the non-functional requirements.

## Derived From

- **Product Spec**: §5.1 Core Feature 11 — Export & Sharing (professional PDF with blueprints, BOM, financial estimates, compliance scorecards)
- **Product Spec**: §5.3 MoSCoW — PDF Export Local (Must Have)
- **Product Spec**: §7 Non-Functional Requirements (PDF generation < 3 seconds)
- **UI Spec**: §4.7 Performance Reports Dashboard Tab (Stitch screens: `4580a1f0`, `5178c6f0`), PDF export action, key metrics grid, blueprint preview
- **Constitution**: Article 1.3 (Android PdfDocument API), Article 4.2 (PDF stored in app-private storage)

## Acceptance Criteria Summary

- [ ] PDF document includes: cover page, scaled 2D blueprint with legend, equipment BOM table, financial estimates (CapEx/OpEx/ROI), and risk/compliance scorecards
- [ ] For "Existing Assessment" projects, the PDF also includes enhancement recommendations with Before/After comparisons
- [ ] PDF is generated entirely offline using Android PdfDocument API with no server dependencies
- [ ] Blueprint canvas is re-rendered at print-appropriate resolution and scale in the PDF
- [ ] PDF generation completes in under 3 seconds on mid-range devices
- [ ] Generated PDFs are stored in app-private storage (Context.filesDir)
- [ ] Users can preview, save (via share intent), or print the generated PDF

## UI Components (if applicable)

- **ReportsTab**: Dashboard Tab 5 composable with assessment summary, key metrics grid, and export actions
- **MetricsSummaryGrid**: Grid cards showing Compliance Score, Total Capacity, Ventilation Output, and Total Cost
- **BlueprintPreviewCard**: Inline scaled preview of the 2D layout with link to full editor
- **PdfExportButton**: Primary action button triggering PDF generation with progress indicator
- **ShareButton**: Secondary action for sharing the generated PDF via Android share intents

## Technical Hints

- Constitution Article 1.3 specifies Android `PdfDocument` API — no external PDF libraries (iText, Apache PDFBox) without a documented justification per Article 1.4
- Constitution Article 4.2 mandates PDF storage in `Context.filesDir`, not external shared storage — use `FileProvider` for sharing via intents
- The blueprint canvas rendering for PDF requires a separate draw pass at a higher DPI (300 DPI recommended for print) — coordinate with the canvas architecture from Feature #008
- Performance target: < 3 seconds on mid-range devices — consider background generation via coroutine with progress callback

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
