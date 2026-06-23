# Tech Stack

## Platform
- **Target:** Android only (phone + tablet)
- **iOS:** Not planned

## Language & Frameworks

| Layer | Technology | Notes |
|---|---|---|
| Language | Kotlin | Primary language |
| UI Framework | Jetpack Compose | Declarative UI, Material 3 |
| Local Database | Room (SQLite) | Offline-first persistence |
| 2D Rendering | Compose Canvas | Interactive house blueprints |
| PDF Generation | Android PdfDocument API | Blueprint and report export |
| Backend | Firebase | Firestore, Auth, Cloud Storage |
| Weather API | OpenWeatherMap (or similar) | Climate zone auto-detection |
| Location | Android Location Services | GPS-based region detection |
| DI | Hilt | Dependency injection |
| Navigation | Compose Navigation | Screen routing |
| Image Loading | Coil | If equipment images needed |

## Architecture Pattern
- **MVVM** (Model-View-ViewModel) with Clean Architecture layers
- **Repository pattern** for data access abstraction
- **Use Cases** for business logic encapsulation

## Project Structure
```
app/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   ├── remote/         # Firebase, Weather API services
│   ├── repository/     # Repository implementations
│   └── catalog/        # Pre-loaded equipment & breed data
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic use cases
├── ui/
│   ├── home/           # Project list screen
│   ├── wizard/         # New/existing project wizard steps
│   ├── dashboard/      # Project dashboard tabs
│   │   ├── blueprint/  # 2D interactive view
│   │   ├── financial/  # Financial projections
│   │   ├── risk/       # Risk assessment
│   │   ├── enhance/    # Enhancement recommendations
│   │   └── report/     # Report generation
│   ├── catalog/        # Equipment & breed catalog browsers
│   ├── settings/       # App settings
│   └── components/     # Shared UI components
├── di/                 # Hilt modules
└── util/               # Utilities, extensions
```

## Key Dependencies (build.gradle)
```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.x"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// Room
implementation("androidx.room:room-runtime:2.6.x")
implementation("androidx.room:room-ktx:2.6.x")
kapt("androidx.room:room-compiler:2.6.x")

// Hilt
implementation("com.google.dagger:hilt-android:2.x")
kapt("com.google.dagger:hilt-android-compiler:2.x")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:32.x"))
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-storage-ktx")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.x")
implementation("androidx.hilt:hilt-navigation-compose:1.1.x")

// Retrofit (Weather API)
implementation("com.squareup.retrofit2:retrofit:2.9.x")
implementation("com.squareup.retrofit2:converter-gson:2.9.x")

// Location
implementation("com.google.android.gms:play-services-location:21.x")
```

## Offline Strategy
1. Room database is the single source of truth
2. All operations work against local Room database
3. When online + cloud sync enabled:
   - Changes are queued and synced to Firestore
   - Conflict resolution: last-write-wins with timestamp
4. Weather API data is cached locally after first fetch
5. Equipment and breed catalogs are bundled with the app (pre-loaded)

## Localization
- French (primary, ships with v1)
- Arabic with RTL support (second phase)
- String resources in `res/values-fr/` and `res/values-ar/`
