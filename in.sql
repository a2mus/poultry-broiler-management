PRAGMA journal_mode=WAL;
PRAGMA foreign_keys=OFF;

CREATE TABLE IF NOT EXISTS breed_profiles (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    breed_name TEXT NOT NULL UNIQUE,
    supplier TEXT NOT NULL,
    growth_targets_json TEXT NOT NULL,
    min_density_kg_m2 REAL NOT NULL,
    max_density_kg_m2 REAL NOT NULL,
    target_fcr REAL NOT NULL,
    cycle_duration_days INTEGER NOT NULL,
    target_weight_g INTEGER NOT NULL,
    mortality_rate_pct REAL NOT NULL,
    description TEXT
);

CREATE UNIQUE INDEX IF NOT EXISTS index_breed_profiles_breed_name ON breed_profiles(breed_name);

CREATE TABLE IF NOT EXISTS equipment_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    brand TEXT,
    model_number TEXT,
    capacity TEXT,
    power_watts REAL,
    unit TEXT NOT NULL,
    coverage_m2 REAL,
    description TEXT
);

CREATE INDEX IF NOT EXISTS idx_equipment_category ON equipment_items(category);

-- Ross 308 breed profile with weekly growth targets
INSERT INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
VALUES (
    'Ross 308',
    'Aviagen',
    '[{"week":1,"targetWeightG":190,"dailyFeedG":25,"dailyWaterMl":45},{"week":2,"targetWeightG":450,"dailyFeedG":50,"dailyWaterMl":90},{"week":3,"targetWeightG":850,"dailyFeedG":85,"dailyWaterMl":150},{"week":4,"targetWeightG":1450,"dailyFeedG":125,"dailyWaterMl":220},{"week":5,"targetWeightG":2150,"dailyFeedG":165,"dailyWaterMl":290},{"week":6,"targetWeightG":2800,"dailyFeedG":200,"dailyWaterMl":350}]',
    30.0,
    42.0,
    1.55,
    42,
    2800,
    4.0,
    'Race de poulet de chair à croissance rapide, leader mondial de l''industrie avicole.'
);

-- Cobb 500 breed profile with weekly growth targets
INSERT INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
VALUES (
    'Cobb 500',
    'Cobb-Vantress',
    '[{"week":1,"targetWeightG":185,"dailyFeedG":24,"dailyWaterMl":43},{"week":2,"targetWeightG":430,"dailyFeedG":48,"dailyWaterMl":86},{"week":3,"targetWeightG":820,"dailyFeedG":82,"dailyWaterMl":145},{"week":4,"targetWeightG":1400,"dailyFeedG":120,"dailyWaterMl":215},{"week":5,"targetWeightG":2080,"dailyFeedG":160,"dailyWaterMl":280},{"week":6,"targetWeightG":2750,"dailyFeedG":195,"dailyWaterMl":340}]',
    30.0,
    40.0,
    1.52,
    42,
    2750,
    3.8,
    'Race de poulet de chair performante, reconnue pour son excellent indice de consommation.'
);

-- VENTILATION
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Ventilateur extracteur 50"', 'VENTILATION', 'Munters', 'EM50-1.5', '35000 m³/h', 750.0, 'unité', 150.0, 'Ventilateur extracteur à hélice pour bâtiments avicoles. Débit d''air élevé avec consommation énergétique optimisée.');

-- FEEDING
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Ligne d''alimentation', 'FEEDING', 'Roxell', 'FL-500', '500 oiseaux', NULL, 'mètre linéaire', NULL, 'Système d''alimentation automatique pour poulets de chair. Distribution uniforme de l''aliment.');

-- HEATING
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Chauffage radiant 20kW', 'HEATING', 'Thermobile', 'THR-20K', '20 kW', 200.0, 'unité', 200.0, 'Chauffage radiant pour poussins. Rayonnement infrarouge direct pour un chauffage uniforme.');

-- LIGHTING
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Tube néon LED 120cm', 'LIGHTING', 'Greengage', 'LED-120-3000K', '18W, 3000K', 18.0, 'unité', 25.0, 'Éclairage LED pour bâtiments avicoles. Spectre lumineux optimisé pour le bien-être des volailles.');

-- COOLING
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Rampe de brumisation', 'COOLING', 'Lubing', 'MIST-4L', '4 L/h', 50.0, 'mètre linéaire', 100.0, 'Système de refroidissement par brumisation pour bâtiments avicoles. Réduction efficace de la température ambiante.');

-- WATERING
INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
VALUES ('Pipette abreuvoir', 'WATERING', 'Plasson', 'NIP-12', '12 oiseaux', NULL, 'unité', NULL, 'Abreuvoir à pipette pour poulets. Débit d''eau constant et hygiénique.');
