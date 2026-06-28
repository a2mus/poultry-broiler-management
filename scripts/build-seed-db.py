import os
import sqlite3
import json

def main():
    db_file = "app/src/main/assets/seed/poultry.db"
    
    # Ensure directory exists
    os.makedirs(os.path.dirname(db_file), exist_ok=True)
    
    # Remove existing db if it exists
    if os.path.exists(db_file):
        os.remove(db_file)
        
    print(f"Creating new seed database at {db_file}...")
    conn = sqlite3.connect(db_file)
    cursor = conn.cursor()
    
    # Set WAL mode and disable foreign keys for setup
    cursor.execute("PRAGMA journal_mode=WAL;")
    cursor.execute("PRAGMA foreign_keys=OFF;")
    
    # Create breed_profiles table
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS breed_profiles (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        breed_name TEXT NOT NULL,
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
    """)
    
    # Create unique index for breed_profiles
    cursor.execute("""
    CREATE UNIQUE INDEX IF NOT EXISTS index_breed_profiles_breed_name ON breed_profiles(breed_name);
    """)
    
    # Create equipment_items table
    cursor.execute("""
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
    """)
    
    # Create index for equipment_items
    cursor.execute("""
    CREATE INDEX IF NOT EXISTS index_equipment_items_category ON equipment_items(category);
    """)
    
    # Insert Ross 308 breed profile
    ross_308_growth = [
        {"week": 1, "targetWeightG": 190, "dailyFeedG": 25, "dailyWaterMl": 45},
        {"week": 2, "targetWeightG": 450, "dailyFeedG": 50, "dailyWaterMl": 90},
        {"week": 3, "targetWeightG": 850, "dailyFeedG": 85, "dailyWaterMl": 150},
        {"week": 4, "targetWeightG": 1450, "dailyFeedG": 125, "dailyWaterMl": 220},
        {"week": 5, "targetWeightG": 2150, "dailyFeedG": 165, "dailyWaterMl": 290},
        {"week": 6, "targetWeightG": 2800, "dailyFeedG": 200, "dailyWaterMl": 350}
    ]
    cursor.execute("""
    INSERT INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """, (
        'Ross 308',
        'Aviagen',
        json.dumps(ross_308_growth, separators=(',', ':')),
        30.0,
        42.0,
        1.55,
        42,
        2800,
        4.0,
        "Race de poulet de chair à croissance rapide, leader mondial de l'industrie avicole."
    ))
    
    # Insert Cobb 500 breed profile
    cobb_500_growth = [
        {"week": 1, "targetWeightG": 185, "dailyFeedG": 24, "dailyWaterMl": 43},
        {"week": 2, "targetWeightG": 430, "dailyFeedG": 48, "dailyWaterMl": 86},
        {"week": 3, "targetWeightG": 820, "dailyFeedG": 82, "dailyWaterMl": 145},
        {"week": 4, "targetWeightG": 1400, "dailyFeedG": 120, "dailyWaterMl": 215},
        {"week": 5, "targetWeightG": 2080, "dailyFeedG": 160, "dailyWaterMl": 280},
        {"week": 6, "targetWeightG": 2750, "dailyFeedG": 195, "dailyWaterMl": 340}
    ]
    cursor.execute("""
    INSERT INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """, (
        'Cobb 500',
        'Cobb-Vantress',
        json.dumps(cobb_500_growth, separators=(',', ':')),
        30.0,
        40.0,
        1.52,
        42,
        2750,
        3.8,
        "Race de poulet de chair performante, reconnue pour son excellent indice de consommation."
    ))
    
    # Insert equipment items
    equipment = [
        ('Ventilateur extracteur 50"', 'VENTILATION', 'Munters', 'EM50-1.5', '35000 m³/h', 750.0, 'unité', 150.0, "Ventilateur extracteur à hélice pour bâtiments avicoles. Débit d'air élevé avec consommation énergétique optimisée."),
        ("Ligne d'alimentation", 'FEEDING', 'Roxell', 'FL-500', '500 oiseaux', None, 'mètre linéaire', None, "Système d'alimentation automatique pour poulets de chair. Distribution uniforme de l'aliment."),
        ('Chauffage radiant 20kW', 'HEATING', 'Thermobile', 'THR-20K', '20 kW', 200.0, 'unité', 200.0, "Chauffage radiant pour poussins. Rayonnement infrarouge direct pour un chauffage uniforme."),
        ('Tube néon LED 120cm', 'LIGHTING', 'Greengage', 'LED-120-3000K', '18W, 3000K', 18.0, 'unité', 25.0, "Éclairage LED pour bâtiments avicoles. Spectre lumineux optimisé pour le bien-être des volailles."),
        ('Rampe de brumisation', 'COOLING', 'Lubing', 'MIST-4L', '4 L/h', 50.0, 'mètre linéaire', 100.0, "Système de refroidissement par brumisation pour bâtiments avicoles. Réduction efficace de la température ambiante."),
        ('Pipette abreuvoir', 'WATERING', 'Plasson', 'NIP-12', '12 oiseaux', None, 'unité', None, "Abreuvoir à pipette pour poulets. Débit d'eau constant et hygiénique.")
    ]
    
    cursor.executemany("""
    INSERT INTO equipment_items (name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
    """, equipment)
    
    conn.commit()
    
    # Output verification
    cursor.execute("SELECT COUNT(*) FROM breed_profiles;")
    print(f"Breed profiles count: {cursor.fetchone()[0]}")
    
    cursor.execute("SELECT COUNT(*) FROM equipment_items;")
    print(f"Equipment items count: {cursor.fetchone()[0]}")
    
    conn.close()
    print("Seed database successfully created!")

if __name__ == '__main__':
    main()
