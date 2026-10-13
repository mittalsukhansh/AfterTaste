# Product Requirements Document (PRD) & Project Brief: AfterTaste

**Project Name:** AfterTaste (Specialty Coffee Journal & Cafe Hopping Companion)  
**Document Version:** 1.0.0  
**Status:** Approved for Implementation / AntiGravity Handoff  
**Target Platform:** Mobile Web & Native App (iOS / Android responsive, ~390–420px viewport)  
**Design System:** Warm Artisanal Nordic (`#2c1810` Espresso / `#c86d3b` Caramel / `#fef8f4` Warm Milk Surface)

---

## 1. Executive Summary & Vision

### 1.1 Product Statement
**AfterTaste** is an artisanal mobile coffee companion designed specifically for **specialty coffee enthusiasts, home brewers, and cafe hoppers**. Unlike generic food logs or overly technical laboratory brewing timers, AfterTaste bridges the gap between personal sensory logs and neighborhood cafe discovery. It allows users to record cups without burdensome barista-only parameters (like kettle temps or grind micron settings when dining out), maintain an inventory of their bean vault, track their evolving sensory palate via a dynamic flavor equilibrium radar, and curate a localized cafe passport of visited roasteries, orders, and ratings.

### 1.2 Target Audience & Personas
1. **The Passionate Cafe Hopper ("Maya")**: Visits specialty roasters across cities (Copenhagen, Tokyo, NYC, London). Wants to remember which lot or single-origin pour-over she drank, what pastry she paired with it, and what rating she gave.
2. **The Mindful Home Brewer**: Buys whole-bean bags from boutique roasteries (Sey, April, Tim Wendelboe, Onyx). Wants to monitor bean resting windows (peak flavor days off roast) and remaining supply.
3. **The Sensory Explorer**: Wants to understand their palate over time—quantifying balance across Acidity, Sweetness, Body, Clarity, and Bitterness without pretension.

---

## 2. Core Value Propositions & Key Features

### 2.1 Daily Brew Journal (Home Feed)
- **Morning Dial-In Greeting**: Time-aware status, weekly rhythm counter, and quick-access bean vault favorites.
- **Quick Log Hero**: Rapid 1-tap call-to-action to capture the morning cup in seconds.
- **Featured Cup Breakdown**: Deep-dive card illustrating the current house favorite lot, roaster, processing method (e.g. Double Anaerobic, Washed, Honey), and sensory spectrum score.
- **Journal History Timeline**: Filterable stream (All, Pour Over, Espresso, Cold Brew) recording timestamp, recipe ratio, flavor descriptors, and half-star ratings.

### 2.2 Cafe Map & Dining Passport (Neighborhood Roaster Exploration)
- **Geographic Exploration & Proximity**: Configurable neighborhood search with radius toggles (e.g., Copenhagen Vesterbro within 3 km).
- **Visited ("Dined In") vs. Wishlist State**: Color-coded pins and cards distinguishing places already visited vs. aspirational spots.
- **Tasted Dossier per Cafe**: Embedded itemized logs of what the user specifically ordered (drinks, hand drips, pastries) along with prices paid and item ratings.
- **Live Menu Fetch**: Quick glance at the cafe’s current offerings, roastery details, opening hours, and community scores.
- **Passport High-Level Metrics**: Running counters of total Cafes Explored, Drinks Tasted, and Average Rating.

### 2.3 Bean & Roaster Vault (Inventory & Bag Tracking)
- **Rotation Management**: Segmentation by *Active Bags*, *Finished*, and *Wishlist*.
- **AI OCR Bag Scanner**: Camera trigger to scan bean bag labels and automatically extract roaster, origin country, variety, process, roast date, and tasting notes.
- **Peak Window & Rest Tracker**: Real-time degassing tracker showing days off roast and optimal extraction windows (e.g., "10d rested • Peak Window").
- **Inventory Depletion Meter**: Visual progress indicator tracking remaining grams against total package weight (e.g., `120g / 250g`).
- **Partner Roastery Directory**: Curated cupping archives of world-class roasters with past harvests tracked.

### 2.4 Sensory & Brew Analytics (Palate Architecture)
- **Enthusiast Coffee Passport Header**: Certification tier (e.g. "Seasoned Cupper", Passport N°), total brew volume, and explored terroir count.
- **Flavor Equilibrium Radar (5-Axis Sensory Polygon)**: Interactive visualization displaying Acidity (%), Sweetness (%), Body (%), Clarity (%), and Bitterness (%).
- **Dominant Aroma Frequency**: Quantified breakdown of recorded flavor descriptors (e.g. Citrus/Bergamot 50%, Jasmine/White Flowers 37%, Dark Chocolate 31%).
- **Terroir Archive & Origin Map**: Exploration tracker by country of origin (Ethiopia, Colombia, Kenya, Guatemala) with elevation indicators.
- **Smart Roast Recommendations**: Algorithmic beans suggested based on high affinity for specific flavor notes.

### 2.5 Cafe-Hopping Brew Logger (Logging Flow)
- **Streamlined for Patrons**: Removed unattainable at-home lab variables (kettle temperature, grind click micro-calibration, 4-stage bloom timers).
- **Cafe & Preparation Selection**: One-tap selection of style: Pour Over, Espresso, Batch Drip, Cold Brew, or Milk Beverage.
- **Sensory Descriptor Pills**: Rapid multi-select tag cloud (Floral & Jasmine, Stone Fruit, Citrus, Dark Chocolate, etc.) with custom tag addition.
- **Scoring & Atmosphere**: 5-star half-step rating, tasting notes memo, atmosphere attributes, and photo attachment for latte art or cup presentation.

---

## 3. Information Architecture & App Navigation

```
AfterTaste App Root
│
├── Top App Bar (Global Header)
│   ├── Brand Monogram & Title ("AfterTaste")
│   ├── Multilingual Switcher ("EN ▾", "DA", "JA", "FR")
│   ├── Theme Mode Toggle (Light Warm Cream / Dark Espresso)
│   ├── Notifications Center
│   └── User Avatar Profile
│
├── Persistent Bottom Navigation Bar (Tabs)
│   ├── [1] Journal (Daily Brew Timeline & Featured Cup)
│   ├── [2] Vault (Bean Inventory, AI Bag OCR, Roasters)
│   ├── [+] Quick Log (Floating Action Button -> Modal Stack Logger)
│   ├── [3] Analytics (Palate Radar, Aroma Frequency, Terroirs)
│   └── [4] Cafes (Map, Visited Dossier, Menus, Passport)
│
└── Stack Modal
    └── Log New Brew (Back Button, Method, Bean, Sensory Tags, Save)
```

---

## 4. UI Design System & Theming Tokens

### 4.1 Typography
- **Primary Serif (Display & Headers)**: `Playfair Display`, serif (`font-serif`) — expresses Nordic artisanal craftsmanship, warmth, and literary coffee culture.
- **Primary Sans (UI & Body Text)**: `Plus Jakarta Sans` or `system-ui`, sans-serif (`font-sans`) — crisp, modern legibility for micro-copy and data points.

### 4.2 Color Palette
| Token Name | Hex Code | Purpose |
|---|---|---|
| `surface` | `#fef8f4` | Primary warm cream canvas background |
| `surface-dim` | `#dfd9d5` | Muted border and divider borders |
| `surface-container-low` | `#f8f2ee` | Subtle card background |
| `surface-container` | `#f3ede8` | Chip, pill, and tertiary button background |
| `surface-container-high`| `#ede7e2` | Active borders and elevated cards |
| `espresso` | `#2c1810` | Primary dark accent, key buttons, main headings |
| `roast` | `#422216` | Secondary body text and subtitles |
| `caramel` | `#c86d3b` | Accent orange/amber for active indicators, pins, badges |
| `AfterTaste` | `#f4ede4` | Soft neutral fills |
| `amber-500` | `#f59e0b` | Star scoring and cupping ratings |
| `emerald-600` | `#059669` | Rested bean indicators & process badges |

---

## 5. Technical Requirements & Handoff Specifications

1. **Framework Compatibility**: Designed for standard HTML5/CSS3, Tailwind CSS (v3.x+), and clean headless component frameworks (React, Vue, Svelte, or direct AntiGravity export).
2. **Icons**: Material Symbols Outlined (`local_cafe`, `inventory_2`, `ssid_chart`, `storefront`, `photo_camera`, `qr_code_scanner`, `tune`, `dark_mode`, `translate`).
3. **Responsive Breakpoints**: Optimized specifically for mobile viewports (`max-w-[420px] mx-auto min-h-screen`) with sticky glassmorphism headers and fixed bottom bars.
4. **Data Models Required**:
   - `BrewLog`: `id`, `beanId`, `cafeId`, `method`, `tastingNotes[]`, `rating`, `notes`, `photoUrl`, `timestamp`.
   - `Bean`: `id`, `roaster`, `origin`, `process`, `variety`, `elevation`, `roastDate`, `weightTotal`, `weightRemaining`, `tastingNotes[]`.
   - `Cafe`: `id`, `name`, `neighborhood`, `distance`, `coordinates`, `visitedStatus`, `userRating`, `communityRating`, `tastedItems[]`, `liveMenu[]`.
   - `UserProfile`: `id`, `name`, `palateLevel`, `totalBrews`, `originsExplored`, `radarMetrics{acidity, sweetness, body, clarity, bitterness}`.

---

## 6. Success Metrics & Roadmap

- **Phase 1 (MVP - Current State)**: 5 screens rendered with persistent shells, verified design system, live mockups, and complete self-contained HTML/CSS source code export.
- **Phase 2 (Functionality)**: Interactive state bindings (theme switching, language i18n dictionary, local storage for brew log entries).
- **Phase 3 (Integration)**: Integration with Geolocation API, Apple Maps/Mapbox SDK for the cafe explorer, and Vision AI model for label OCR extraction.
