# Build Seed Database

This script generates the pre-populated SQLite seed database used by Room's `createFromAsset()`.

## Requirements

- `sqlite3` command-line tool

## Usage

### macOS/Linux
```bash
bash scripts/build-seed-db.sh
```

### Windows (PowerShell)
```powershell
# Install sqlite3 if needed: winget install sqlite.sqlite
# Then use the PowerShell script:
pwsh scripts/build-seed-db.ps1
```

## What it creates

Creates `app/src/main/assets/seed/poultry.db` with:
- 2 breed profiles (Ross 308, Cobb 500) with growth target data
- 6 equipment items across all categories (VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING)

The database schema must exactly match the Room entity definitions in:
- `app/src/main/java/com/poultry/broiler/data/local/entity/BreedProfileEntity.kt`
- `app/src/main/java/com/poultry/broiler/data/local/entity/EquipmentItemEntity.kt`
