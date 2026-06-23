# Data Model

## Entity Relationship Overview

```
Project (1) ──── (1) House
    │                │
    │                └──── (1) VentilationSystem
    │
    ├──── (1) BreedConfig
    ├──── (*) ProjectEquipment
    ├──── (1) FinancialEstimate
    ├──── (1) RiskAssessment
    ├──── (*) Enhancement          (existing installations only)
    └──── (*) Report
```

## Core Entities

### Project
The root entity. Represents a single farm design or assessment job.

| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| name | String | Project name |
| type | Enum | `NEW_INSTALLATION` or `EXISTING_ASSESSMENT` |
| status | Enum | `DRAFT`, `IN_PROGRESS`, `COMPLETED` |
| farmerId | UUID? | Optional link to farmer |
| consultantId | UUID? | Optional link to consultant |
| locationLat | Double? | GPS latitude |
| locationLng | Double? | GPS longitude |
| region | String | Region name |
| climateZoneId | String | FK to ClimateZone |
| createdAt | Long | Timestamp |
| updatedAt | Long | Timestamp |
| syncedAt | Long? | Last cloud sync timestamp |

### House
Physical house structure details.

| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| length | Double | House length (meters) |
| width | Double | House width (meters) |
| height | Double | Wall height (meters) |
| ridgeHeight | Double? | Ridge height for pitched roof |
| orientation | Enum | `NORTH_SOUTH`, `EAST_WEST`, `CUSTOM` |
| insulationType | Enum | `NONE`, `POLYSTYRENE`, `POLYURETHANE`, `SANDWICH_PANEL` |
| insulationThickness | Double? | In millimeters |
| roofType | Enum | `FLAT`, `PITCHED`, `ARCHED` |
| floorType | Enum | `CONCRETE`, `DIRT`, `SLAT` |
| wallMaterial | Enum | `BLOCK`, `STEEL`, `PREFAB` |
| capacity | Int | Number of birds |
| totalArea | Double | Calculated (length * width) |

### VentilationSystem
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| houseId | UUID | FK to House |
| type | Enum | `TUNNEL`, `CROSS_VENTILATION`, `NATURAL`, `HYBRID` |
| fanCount | Int | Number of exhaust fans |
| fanDiameter | Double | Fan diameter (inches) |
| fanCfm | Double | Airflow per fan (CFM) |
| fanPlacement | String (JSON) | Array of placement positions |
| inletCount | Int | Number of air inlets |
| inletType | Enum | `SIDE_WALL`, `CEILING`, `TUNNEL_END` |
| inletPlacement | String (JSON) | Array of placement positions |
| hasCoolingPad | Boolean | Evaporative cooling installed |
| coolingPadType | String? | Pad material/brand |
| coolingPadArea | Double? | Total pad area (sq meters) |
| hasHeater | Boolean | Heating system present |
| heaterType | String? | Heater type |
| heaterCount | Int | Number of heaters |
| estimatedMinVentilation | Double | Minimum ventilation rate (CFM) |
| estimatedMaxVentilation | Double | Maximum ventilation rate (CFM) |

### ProjectEquipment
Equipment selected for this project.

| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| catalogItemId | UUID | FK to EquipmentCatalog |
| category | Enum | `FEEDING`, `WATERING`, `HEATING`, `COOLING`, `LIGHTING`, `CURTAIN`, `OTHER` |
| name | String | Equipment name |
| quantity | Int | Number of units |
| unitPrice | Double | Price per unit |
| totalPrice | Double | quantity * unitPrice |
| specifications | String (JSON) | Custom specs |
| notes | String? | Notes |

### BreedConfig
Breed selected for this project with its requirements.

| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| breedId | UUID | FK to BreedCatalog |
| breedName | String | Breed display name |
| spacePerBird | Double | Required space (sq meters per bird) |
| feedConversionRatio | Double | Feed conversion ratio |
| targetWeight | Double | Target weight at slaughter (kg) |
| growthDays | Int | Days to reach target weight |
| tempMin | Double | Minimum comfortable temperature (C) |
| tempMax | Double | Maximum comfortable temperature (C) |
| humidityMin | Double | Min humidity (%) |
| humidityMax | Double | Max humidity (%) |
| ventilationPerBird | Double | Required airflow per bird (CFM) |

### FinancialEstimate
Projected costs and revenue.

| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| constructionCost | Double | Total construction cost |
| equipmentCost | Double | Total equipment cost |
| totalInvestment | Double | constructionCost + equipmentCost |
| electricityCostPerCycle | Double | Estimated electricity per flock cycle |
| feedCostPerCycle | Double | Estimated feed cost per cycle |
| laborCostPerCycle | Double | Estimated labor cost |
| otherCostsPerCycle | Double | Miscellaneous costs |
| totalCostPerCycle | Double | Sum of all per-cycle costs |
| revenuePerCycle | Double | Projected income per flock |
| profitPerCycle | Double | Revenue - total cost per cycle |
| costPerBird | Double | Total cost / capacity |
| revenuePerBird | Double | Revenue / capacity |
| cyclesPerYear | Int | Number of flock cycles per year |
| annualProfit | Double | profitPerCycle * cyclesPerYear |
| roiMonths | Int | Months to recoup investment |

### RiskAssessment
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| overallRiskLevel | Enum | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| mortalityRiskScore | Int | 0-100 |
| mortalityRiskFactors | String (JSON) | Array of {factor, severity, description} |
| welfareComplianceScore | Int | 0-100 |
| complianceGaps | String (JSON) | Array of {standard, gap, recommendation} |
| environmentalRiskScore | Int | 0-100 |
| environmentalFactors | String (JSON) | Array of {factor, severity, description} |
| ventilationAdequacy | Enum | `ADEQUATE`, `MARGINAL`, `INADEQUATE` |
| densityCompliance | Boolean | Birds/sqm within standards |

### Enhancement (Existing Installations Only)
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| category | Enum | `VENTILATION`, `INSULATION`, `EQUIPMENT`, `STRUCTURE`, `BIOSECURITY` |
| title | String | Enhancement title |
| description | String | Detailed description |
| priority | Enum | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| estimatedCost | Double | Cost of implementation |
| estimatedBenefit | String | Expected benefit description |
| riskReduction | Int | Risk reduction points (0-100) |
| roi | Double? | Return on this specific enhancement |

### Report
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| projectId | UUID | FK to Project |
| type | Enum | `FULL`, `BLUEPRINT_ONLY`, `FINANCIAL_ONLY`, `RISK_ONLY` |
| format | String | `PDF` |
| filePath | String | Local file path |
| generatedAt | Long | Timestamp |
| shared | Boolean | Whether shared via cloud |
| shareUrl | String? | Cloud share URL if shared |

## Reference Data (Pre-loaded Catalogs)

### EquipmentCatalog
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| name | String | Equipment name |
| category | Enum | Same as ProjectEquipment |
| manufacturer | String? | Manufacturer |
| model | String? | Model number |
| defaultPrice | Double | Default price |
| specifications | String (JSON) | Technical specs |
| isCustom | Boolean | User-added item |
| updatedAt | Long | Last price update |

### BreedCatalog
| Field | Type | Description |
|---|---|---|
| id | UUID | Primary key |
| name | String | Breed name (e.g., Ross 308, Cobb 500) |
| origin | String | Country/company of origin |
| description | String | Breed characteristics |
| spacePerBird | Double | sq meters per bird |
| feedConversionRatio | Double | FCR |
| targetWeight | Double | kg at slaughter |
| growthDays | Int | Days to market weight |
| tempMin | Double | Min comfortable temp (C) |
| tempMax | Double | Max comfortable temp (C) |
| humidityMin | Double | Min humidity (%) |
| humidityMax | Double | Max humidity (%) |
| ventilationPerBird | Double | CFM per bird |
| mortalityRate | Double | Expected mortality % |

### ClimateZone
| Field | Type | Description |
|---|---|---|
| id | String | Zone identifier |
| name | String | Zone name |
| region | String | Geographic region |
| country | String | Country |
| avgTempByMonth | String (JSON) | 12-element array of avg temps |
| avgHumidityByMonth | String (JSON) | 12-element array of avg humidity |
| extremeHeatDays | Int | Days above 40C per year |
| extremeColdDays | Int | Days below 5C per year |
| recommendedVentilationType | Enum | Suggested ventilation type |
| description | String | Climate characteristics |
