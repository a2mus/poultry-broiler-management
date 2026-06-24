# Data Model: Project Scaffolding & Design System

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`

---

## Entity Relationship Overview

```
┌─────────────────────┐
│   BreedProfile      │
│─────────────────────│
│ PK id: Long         │
│    breedName: String│
│    ...              │
└─────────────────────┘

┌─────────────────────┐
│   EquipmentItem     │
│─────────────────────│
│ PK id: Long         │
│    name: String     │
│    category: String │
│    ...              │
└─────────────────────┘
```

> No foreign key relationships between entities in this scaffolding feature. Both entities are independent reference data loaded from seed assets. Cross-entity relationships (e.g., breed → recommended equipment) will be introduced in subsequent features.

---

## Entity: BreedProfile

Represents a poultry broiler breed with its growth characteristics, density requirements, and performance targets. Pre-seeded with Ross 308 and Cobb 500.

### Room Entity: `BreedProfileEntity`

| Column | Type | Nullable | Constraints | Description |
|--------|------|----------|-------------|-------------|
| `id` | `Long` | No | PK, auto-generate | Unique identifier |
| `breed_name` | `String` | No | UNIQUE | Breed commercial name (e.g., "Ross 308") |
| `supplier` | `String` | No | — | Breed supplier/company (e.g., "Aviagen", "Cobb-Vantress") |
| `growth_targets_json` | `String` | No | — | JSON array of weekly growth targets (see GrowthTarget below) |
| `min_density_kg_m2` | `Double` | No | > 0 | Minimum recommended density in kg/m² |
| `max_density_kg_m2` | `Double` | No | > min | Maximum recommended density in kg/m² |
| `target_fcr` | `Double` | No | > 0 | Target feed conversion ratio (kg feed / kg weight) |
| `cycle_duration_days` | `Int` | No | > 0 | Standard production cycle in days |
| `target_weight_g` | `Int` | No | > 0 | Target market weight in grams at end of cycle |
| `mortality_rate_pct` | `Double` | No | 0..100 | Expected mortality rate as percentage |
| `description` | `String` | Yes | — | Optional breed notes or description |

**Indices**: `UNIQUE INDEX idx_breed_name ON BreedProfileEntity(breed_name)`

### Embedded Value: GrowthTarget (JSON serialized)

Stored as JSON string in `growth_targets_json`, deserialized via Room `@TypeConverter`.

| Field | Type | Description |
|-------|------|-------------|
| `week` | `Int` | Week number (1-based) |
| `target_weight_g` | `Int` | Expected weight in grams at end of week |
| `daily_feed_g` | `Int` | Recommended daily feed intake in grams |
| `daily_water_ml` | `Int` | Recommended daily water intake in milliliters |

### Domain Model: `BreedProfile`

| Property | Type | Maps From |
|----------|------|-----------|
| `id` | `Long` | `id` |
| `breedName` | `String` | `breed_name` |
| `supplier` | `String` | `supplier` |
| `growthTargets` | `List<GrowthTarget>` | `growth_targets_json` (deserialized) |
| `densityRange` | `ClosedFloatingPointRange<Double>` | `min_density_kg_m2..max_density_kg_m2` |
| `targetFcr` | `Double` | `target_fcr` |
| `cycleDurationDays` | `Int` | `cycle_duration_days` |
| `targetWeightGrams` | `Int` | `target_weight_g` |
| `mortalityRatePercent` | `Double` | `mortality_rate_pct` |
| `description` | `String?` | `description` |

### Seed Data

| breed_name | supplier | min_density | max_density | target_fcr | cycle_days | target_weight_g | mortality_pct |
|-----------|----------|-------------|-------------|------------|------------|-----------------|---------------|
| Ross 308 | Aviagen | 30.0 | 42.0 | 1.55 | 42 | 2800 | 4.0 |
| Cobb 500 | Cobb-Vantress | 30.0 | 40.0 | 1.52 | 42 | 2750 | 3.8 |

---

## Entity: EquipmentItem

Represents a piece of poultry house equipment with specifications and catalog information. Pre-seeded from equipment catalog assets.

### Room Entity: `EquipmentItemEntity`

| Column | Type | Nullable | Constraints | Description |
|--------|------|----------|-------------|-------------|
| `id` | `Long` | No | PK, auto-generate | Unique identifier |
| `name` | `String` | No | — | Equipment name (e.g., "Ventilateur extracteur 50"") |
| `category` | `String` | No | — | Category enum value: VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING |
| `brand` | `String` | Yes | — | Manufacturer/brand name |
| `model_number` | `String` | Yes | — | Manufacturer model reference |
| `capacity` | `String` | Yes | — | Capacity specification (e.g., "35000 m³/h", "500 oiseaux") |
| `power_watts` | `Double` | Yes | >= 0 | Power consumption in watts |
| `unit` | `String` | No | — | Unit of measurement for ordering (e.g., "unité", "mètre linéaire", "lot") |
| `coverage_m2` | `Double` | Yes | > 0 | Area coverage per unit in m² (for density calculations) |
| `description` | `String` | Yes | — | Technical description or notes (in French) |

**Indices**: `INDEX idx_equipment_category ON EquipmentItemEntity(category)`

### Domain Model: `EquipmentItem`

| Property | Type | Maps From |
|----------|------|-----------|
| `id` | `Long` | `id` |
| `name` | `String` | `name` |
| `category` | `EquipmentCategory` | `category` (enum conversion) |
| `brand` | `String?` | `brand` |
| `modelNumber` | `String?` | `model_number` |
| `capacity` | `String?` | `capacity` |
| `powerWatts` | `Double?` | `power_watts` |
| `unit` | `String` | `unit` |
| `coverageM2` | `Double?` | `coverage_m2` |
| `description` | `String?` | `description` |

### Domain Enum: `EquipmentCategory`

```
VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING
```

> Pure Kotlin enum in the domain layer — no Android framework dependency.

### Seed Data (representative sample)

| name | category | brand | capacity | power_watts | unit | coverage_m2 |
|------|----------|-------|----------|-------------|------|-------------|
| Ventilateur extracteur 50" | VENTILATION | Munters | 35000 m³/h | 750.0 | unité | 150.0 |
| Chauffage radiant 20kW | HEATING | Thermobile | 20 kW | 200.0 | unité | 200.0 |
| Ligne d'alimentation | FEEDING | Roxell | 500 oiseaux | null | mètre linéaire | null |
| Rampe de brumisation | COOLING | Lubing | 4 L/h | 50.0 | mètre linéaire | 100.0 |
| Pipette abreuvoir | WATERING | Plasson | 12 oiseaux | null | unité | null |
| Tube néon LED 120cm | LIGHTING | Greengage | 18W, 3000K | 18.0 | unité | 25.0 |

---

## Entity: NavigationRoute (Runtime only — not persisted)

Represents a destination in the app's navigation graph. This is a **sealed class** (not a Room entity) used only at runtime for type-safe navigation.

### Sealed Class: `NavRoute`

| Route | Path String | Icon | Label Resource |
|-------|-------------|------|----------------|
| `Home` | `"home"` | `Icons.Filled.Home` | `R.string.nav_home` |
| `Wizard` | `"wizard"` | `Icons.Filled.AutoAwesome` | `R.string.nav_wizard` |
| `Dashboard` | `"dashboard"` | `Icons.Filled.Dashboard` | `R.string.nav_dashboard` |
| `Catalog` | `"catalog"` | `Icons.Filled.Inventory2` | `R.string.nav_catalog` |
| `Settings` | `"settings"` | `Icons.Filled.Settings` | `R.string.nav_settings` |

---

## Room Database Configuration

| Property | Value |
|----------|-------|
| Database name | `poultry.db` |
| Version | 1 |
| Entities | `BreedProfileEntity`, `EquipmentItemEntity` |
| TypeConverters | `GrowthTargetListConverter` (JSON ↔ `List<GrowthTarget>`) |
| Seed mechanism | `createFromAsset("seed/poultry.db")` |
| Export schema | `true` (for migration validation) |

### TypeConverter: GrowthTargetListConverter

Converts between `String` (JSON array) and `List<GrowthTarget>` using `kotlinx.serialization` or manual JSON parsing (no Gson/Moshi dependency needed for this simple structure).

---

## Validation Rules

| Entity | Field | Rule |
|--------|-------|------|
| BreedProfile | `breedName` | Non-empty, unique |
| BreedProfile | `minDensity` | > 0, < maxDensity |
| BreedProfile | `maxDensity` | > minDensity |
| BreedProfile | `targetFcr` | > 0 |
| BreedProfile | `cycleDurationDays` | > 0 |
| BreedProfile | `targetWeightGrams` | > 0 |
| BreedProfile | `mortalityRatePercent` | 0.0..100.0 |
| EquipmentItem | `name` | Non-empty |
| EquipmentItem | `category` | Valid EquipmentCategory enum value |
| EquipmentItem | `unit` | Non-empty |
| EquipmentItem | `powerWatts` | >= 0 (if provided) |
| EquipmentItem | `coverageM2` | > 0 (if provided) |

> Validation is enforced at the Domain layer (repository/use case preconditions) per Constitution Art 4.3. Seed data is assumed valid since it is developer-controlled and shipped as a read-only asset.
