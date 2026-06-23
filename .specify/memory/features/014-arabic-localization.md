# Feature Brief: Arabic Localization & RTL Support

**Sequence**: #014 of 14
**Category**: :shield: Cross-Cutting
**Priority**: Should Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System
- **Unlocks**: None (terminal feature)
- **Parallelizable with**: Any feature after #001 (ideally started after a few UI features establish patterns — recommend after #005)

## Feature Description

The application's primary market is French-speaking consultants and farmers in Algeria and neighboring regions, but a significant portion of the target user base — particularly farmers and local agricultural agents — operates primarily in Arabic. This feature delivers full Arabic language support with proper right-to-left (RTL) layout rendering, making the application accessible to Arabic-speaking users across the MENA region.

Arabic localization goes far beyond simple string translation. The entire UI layout must mirror for RTL reading direction: the navigation drawer opens from the right edge, the hamburger menu and close buttons are positioned on the right, bottom navigation bar tabs read from right-to-left, and the wizard's Next/Back buttons are flipped (Next on the left pointing left, Back on the right). Every screen, dialog, button, label, status badge, and error message must have an Arabic translation in `values-ar/strings.xml`.

Special attention is required for mixed-content scenarios. Numeric data (dimensions in meters, financial figures in DZD, percentages) uses Arabic-Indic numerals in some locales but Western Arabic numerals in others — the implementation should respect the device's locale settings. The 2D Blueprint Canvas coordinates remain mathematically consistent regardless of text direction (the house origin stays at the physical bottom-left corner), but labels and legends must render in Arabic.

This feature is categorized as "Should Have" because it is not required for the initial launch validation in the French-speaking consultant market, but it is essential for broader market penetration in Algeria and the wider MENA region. The product spec designates it for Phase 2, but the localization resource structure is prepared from day one (Feature #001) to ensure Arabic support can be layered in without architectural changes.

## Derived From

- **Product Spec**: §5.2 Secondary Feature 3 — Arabic Localization (RTL layout, full translation files, Phase 2)
- **Product Spec**: §5.3 MoSCoW — Arabic Language Support (Should Have)
- **Product Spec**: §7 Non-Functional Requirements — Localization (French primary, Arabic RTL secondary)
- **UI Spec**: §6 Accessibility & Localization (§6.2 Localization & RTL Support — dir="rtl", navigation mirroring, button flipping, bilingual assets)
- **UI Spec**: §4.1–§4.7 (Arabic Stitch screen IDs for all 7 core views)
- **Constitution**: Article 8 Localization (§8.1 Language Support, §8.2 Localization Rules — string resources, numeric/date formatting, RTL rules)

## Acceptance Criteria Summary

- [ ] All user-facing strings have Arabic translations in `values-ar/strings.xml`
- [ ] RTL layout is fully functional: navigation drawer from right, mirrored bottom nav, flipped wizard buttons
- [ ] Status badges display correct Arabic text (e.g., "مسودة" for Draft, "مكتمل" for Completed)
- [ ] Numeric formatting respects device locale settings for Arabic-Indic vs. Western Arabic numerals
- [ ] Date formatting uses locale-aware patterns for Arabic calendar display
- [ ] Blueprint Canvas labels and legends render in Arabic while maintaining correct coordinate orientation
- [ ] The app can be switched between French and Arabic via device language settings

## Technical Hints

- Constitution Article 8.2 prohibits hardcoded strings in composables — all text must come from string resources
- Constitution Article 8.2 requires `DateTimeFormatter` with locale-aware patterns and proper decimal separators
- RTL support is enabled via `android:supportsRtl="true"` in the manifest and tested via forced RTL in developer options
- The Blueprint Canvas must decouple label text direction from coordinate system direction — labels follow locale, coordinates stay consistent
- Consider using Compose's `CompositionLocalLayoutDirection` for programmatic RTL testing in UI tests

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
