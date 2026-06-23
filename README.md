# Poultry Broiler House Design & Management

Smart system for designing and managing poultry broiler houses.

## Overview

A mobile application for consultants, design companies, and farmers to design new poultry broiler houses or assess existing installations. The app provides blueprints, equipment recommendations, cost estimates, risk assessments, and welfare compliance evaluations.

## Target Users

- **Consultants / Design companies** who visit farmer sites to design or assess poultry houses
- **Farmers** who want to build a new poultry house or evaluate an existing one

## Application Modes

### 1. New Installation

Design a poultry house from scratch.

**Inputs:**
- House dimensions
- Ventilation placement and type
- Equipment selection
- Climate zone (auto-detected or manual)
- Broiler breed selection

**Outputs:**
- House blueprints (PDF export, then interactive 2D view for adjustments)
- Equipment bill of materials (BOM)
- Cost estimates (construction, equipment, projected electricity consumption)
- Projected revenue and ROI

### 2. Existing Installation Assessment

Evaluate an existing poultry house's design and equipment.

**Inputs:**
- Current house dimensions and layout
- Existing equipment inventory
- Current operational parameters

**Outputs:**
- Welfare compliance evaluation (EU welfare directives)
- Risk assessment (mortality risk, environmental risks)
- Recommended enhancements
- Cost estimates for improvements
- Before/after comparison (current state vs. proposed enhancements)

## Core Modules

### House Designer
- Define house dimensions, insulation, equipment layout
- Ventilation system selection adapted to the climate zone
- Equipment placement and specification
- Breed-specific requirements (space, ventilation, feed, climate needs)
- Suggests optimal ventilation system with cost estimation and equipment list

### Financial Dashboard
- Projected revenue
- ROI on upgrades
- Electricity consumption cost estimates
- Cost per bird estimates
- Construction and equipment cost breakdown

### Risk Assessment
- Chicken mortality risk estimation
- Welfare compliance gap analysis
- Environmental risk factors
- Equipment failure risk

### Reporting
- Flock performance estimations and projections (not live data)
- Cost projection graphs and comparisons
- Scenario comparison (e.g., before/after for existing installations)
- Exportable reports

## Technical Decisions

### Platform
- Mobile app: phone and tablet
- Offline-capable (required for rural farm visits with no internet)

### Data Sources
- **Weather/Climate:** Fetch from weather API + GPS-based auto-detection when available, manual region/climate zone selection as fallback
- **Equipment:** Pre-loaded catalog that users can customize, extend, and update prices
- **Broiler breeds:** Built-in breed database with different space, ventilation, feed, and climate requirements

### Blueprint Output
1. **PDF export** - Initial design output for sharing and review
2. **Interactive 2D view** - In-app adjustments and refinements after initial PDF generation

### Data Storage
- Local storage on device (primary)
- Optional cloud sync when online
- Report sharing capability when connected

### Languages
1. French (primary)
2. Arabic (second phase)

### Compliance Standards
- EU welfare directives (initial)
- Algerian local standards (to be added when available)

## Design Principles

- **Estimation-based:** This is a design and planning tool, not a real-time monitoring system. All projections (electricity costs, mortality risk, revenue, flock performance) are estimations.
- **No real-time sensors:** No IoT or sensor integration. Data is entered manually or fetched from reference databases.
- **No alerts/notifications:** Not applicable for a design-time tool.
- **Offline-first:** Core features must work without internet connectivity.

## Documentation

- [Tech Stack](docs/TECH_STACK.md) - Technology choices and architecture
- [Data Model](docs/DATA_MODEL.md) - Database entities and relationships
- [Screen Flows](docs/SCREENS.md) - UI structure and screen specifications

## Status

Project is in specification phase.
