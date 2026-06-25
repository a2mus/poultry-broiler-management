# App Fonts

This document lists the font files required by the Poultry Broiler Management app.
These fonts are mandated by the project Constitution's typography tokens
(Outfit for display/headers, Inter for body/data).

> **Note**: Font documentation lives here in `docs/`, **not** in
> `app/src/main/res/font/`. Android's resource merger (AAPT2) rejects any file
> in `res/font/` that does not end with `.xml`, `.ttf`, `.ttc`, or `.otf`, so
> README/markdown files must be kept out of that directory.

## Required Fonts

### Outfit (SIL Open Font License)

Download from Google Fonts: <https://fonts.google.com/specimen/Outfit>

Required weights:

- `outfit_regular.ttf` (Weight 400)
- `outfit_medium.ttf` (Weight 500)
- `outfit_semibold.ttf` (Weight 600)
- `outfit_bold.ttf` (Weight 700)

### Inter (SIL Open Font License)

Download from Google Fonts: <https://fonts.google.com/specimen/Inter>

Required weights:

- `inter_regular.ttf` (Weight 400)
- `inter_medium.ttf` (Weight 500)
- `inter_semibold.ttf` (Weight 600)
- `inter_bold.ttf` (Weight 700)

## Installation

Place the downloaded `.ttf` files directly into `app/src/main/res/font/`.
If the directory does not yet exist, create it — it will be picked up by the
build automatically once it contains valid font files. Do **not** add a
`.gitkeep` or any non-font file to keep it tracked; AAPT2 will reject it.
