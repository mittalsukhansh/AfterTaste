# Project Plan

AfterTaste — A personal Android app to log cafe visits: where you went, what you ordered, and how you'd rate it.

Key Details from Project Spec:
- MVVM Architecture + Room DB + Jetpack Compose (Material 3) + Coil + Navigation.
- Data Model: CafeVisit, OrderedItem, Tag, Photo entities. Aggregated Cafe concept.
- Features:
  - Onboarding flow with visual carousel based on reference style (Discover, Log Sips, Coffee Passport).
  - Core logging (Add/Edit/Delete visit, Cafe name, location, items ordered with prices/ratings, total price, auto price-to-quality ratio, date/time).
  - Rating system (Overall rating 0-5 half-star support, sub-scores for taste, ambience, seating, wifi, service, noise level; 'Would return' toggle; 'Best for' tags).
  - Context & discovery (Wifi quality + password, power outlets, seating type, crowd level).
  - Repeat visits & aggregation (Group visits under Cafe view, average rating, visit count, rating trend line chart).
  - Wishlist ("Want to try" cafes, convert to entry once visited).
  - Media: Attach photos per visit (local storage).
  - Dashboard & Stats: Total cafes visited, total spend, monthly visits, favorite cafe, streak counter.
  - Map View: Pins for visited cafes.
  - Data Export/Backup: CSV export, local Room DB backup/restore.
  - Search/Filter by name, tag, rating range, date range.
- Screens:
  1. Onboarding Screen (Welcome flow with pagination pill indicators)
  2. Home / Dashboard (Stats summary, recent visits, quick add)
  3. Add/Edit Visit (Multi-step or single scroll form with coffee bean/star ratings, chips, photo picker)
  4. Cafe List & Cafe Detail (Aggregated cafes, rating trend chart, visit history)
  5. Visit Detail (Full details of single visit)
  6. Map View
  7. Wishlist
  8. Search & Filter
  9. Settings (Export/backup, theme)

## Project Brief

# Project Brief: AfterTaste

**AfterTaste** is a personal Android app designed for coffee lovers and cafe enthusiasts to track cafe visits, log ordered items, rate experiences across key metrics, and discover personal coffee habits.

---

## Features

1. **Onboarding Carousel & Warm Coffee Theme**: A multi-slide onboarding flow (Discover, Log Sips, Coffee Passport) featuring a warm watercolor coffee aesthetic (brown/espresso tones `#986252`), rounded card UI, custom pagination pill indicators, and circular chevron action buttons.
2. **Core Visit Logging & Multi-Metric Rating**: Log visits with cafe name, location, date/time, photos, ordered items (with price and price-to-quality ratio), overall rating (0–5 half-star support), sub-scores (taste, ambience, seating, wifi, service, noise), and practical context (wifi password, power outlets, "would return" toggle, "best for" tags).
3. **Cafe Aggregation & Detail Profiles**: Automatically aggregate individual visits under unified Cafe profiles displaying average ratings, visit counts, rating trend line charts, and historic visit lists.
4. **Dashboard & Stats Summary**: Home screen with high-level stats (total cafes visited, total spend, monthly visits, favorite cafe, streak counter), recent visits list, and quick-add visit trigger.
5. **Interactive Map & Wishlist**: Interactive map view pinning visited cafes, alongside a "Want to Try" wishlist allowing one-tap conversion to a logged visit.

---

## High-Level Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3 (custom warm brown/terracotta palette `#986252`)
- **Navigation**: Jetpack Navigation 3 (state-driven)
- **Adaptive Strategy**: Compose Material Adaptive library
- **Architecture**: MVVM (Model-View-ViewModel) architecture with Kotlin Coroutines & Flow
- **Persistence Layer**: Room Database (storing entities for `CafeVisit`, `OrderedItem`, `Tag`, `Photo`, and handling CSV export/backup)
- **Media**: Coil for asynchronous image loading

## Implementation Steps

### Task_1_DataLayer_Setup: Set up Room Database entities (CafeVisit, Cafe, OrderedItem, WishlistCafe), DAOs, Repository layer, and ViewModel architecture.
- **Status:** COMPLETED
- **Updates:** Task_1_DataLayer_Setup completed successfully: Room entities (CafeVisit, Cafe, OrderedItem, WishlistCafe, Tag, Photo) and DAOs, CafeRepositoryImpl, CafeViewModel, and unit tests created and building clean.
- **Acceptance Criteria:**
  - Room database entities and DAOs created
  - Repository layer handling visit logging and cafe aggregation
  - Build passes cleanly

### Task_2_Theme_And_Onboarding: Implement warm coffee/terracotta Material 3 theme (#986252) and build the Onboarding flow with carousel slides and pill indicators.
- **Status:** COMPLETED
- **Updates:** Task 2 completed: Warm coffee theme (#986252 clay background, #2C1A11 espresso text, #D4A373 terracotta accent, 28-32dp rounded cards) and Onboarding Screen with 3 carousel slides, custom vector illustrations, active pill indicator, circular chevron CTA button, and preference persistence created. Build passed cleanly.
- **Acceptance Criteria:**
  - Warm coffee brown/terracotta theme configured
  - Multi-slide Onboarding carousel built with navigation
  - Build passes cleanly

### Task_3_Visit_Logging_And_Aggregation: Build Visit Logging screen with multi-metric ratings, ordered items, sub-scores, tags, and Cafe aggregation detail screens.
- **Status:** COMPLETED
- **Updates:** Task 3 completed: Implemented CoffeeBeanRatingBar with half-bean support, Add/Edit Visit Screen with sub-scores, tags, ordered items, wifi/power outlet details, photo attachment, price-to-quality ratio, and journal notes. Created Cafe List with sorting/filtering, Cafe Detail screen with custom Canvas rating trend line chart and photo gallery, and Visit Detail screen with Passport stamp badge and edit/delete capabilities. Build passed and 10 unit tests passed.
- **Acceptance Criteria:**
  - Visit logging UI created supporting sub-scores, prices, and photo attachment
  - Cafe aggregation logic and detailed cafe profile view operational
  - Build passes cleanly

### Task_4_Dashboard_Wishlist_Export: Build Dashboard screen with statistics, Wishlist feature, Search/Filter, and CSV export / backup capabilities.
- **Status:** COMPLETED
- **Updates:** Task 4 completed: Created DashboardScreen with key stats (Total Cafes Visited, Total Spend, Visits This Month, Favorite Cafe, Streak Counter), Coffee Passport badges/stamps, and recent visit logs. Implemented WishlistScreen with Convert-To-Visit flow, MapViewScreen with interactive canvas coffee pins and popup cards, SearchFilterScreen with query search and filters (rating, tags, power outlets, wifi, noise level), SettingsScreen with CSV export, JSON backup & restore, onboarding reset, and warm coffee styled Bottom Navigation bar. Build passed and 13 unit tests passed.
- **Acceptance Criteria:**
  - Dashboard screen displaying total spend, stats, and recent visits
  - Wishlist management with convert-to-visit action
  - Search/Filter and CSV export functionality working
  - Build passes cleanly

### Task_5_Run_And_Verify: Run and verify application stability, confirm alignment with user requirements, verify warm coffee UI styling, ensure app does not crash, and make sure all existing tests pass.
- **Status:** COMPLETED
- **Updates:** Task_5_Run_And_Verify completed: Application stability verified on device RZCWB0784KJ with zero crashes. All 13 unit tests passed. Onboarding flow, coffee bean rating component, dashboard stats & badges, wishlist convert-to-visit flow, custom canvas line chart, search/filter, CSV export, and JSON backup verified end-to-end.
- **Acceptance Criteria:**
  - App builds and runs cleanly without crashes
  - Make sure all existing tests pass
  - Instruct critic_agent to verify application stability (no crashes), confirm alignment with user requirements, and report critical UI issues
- **Duration:** N/A

