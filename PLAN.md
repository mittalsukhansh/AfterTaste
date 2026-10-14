# ☕ AfterTaste v2.0.0 — Android Studio Master Plan & Architecture Guide

---

## 📌 Executive Overview: Why You Didn't See the UI Updates & How We Fixed It

In earlier iterations, several v2.0.0 features (interactive brew timers, currency updates to ₹ INR, Leaflet maps, and label scanning) were demonstrated in the **Web Companion** (`web/index.html`). 

Because Android Studio compiles native **Kotlin Jetpack Compose** code from `app/src/main/java/com/aftertaste`, the Android app remained on the older Compose UI until the native screens were synchronized.

### ✅ What Has Been Synchronized Directly in Native Jetpack Compose:
1. **Interactive Brew Guide & Live Pour Timer** (`BrewTimerDialog.kt`):
   - Step-by-step timer with bloom, first pour, second pour, and drawdown stages.
   - Dynamic ratio calculator: Adjust dose (10g–40g) and automatically recalculate water weights and timing.
   - Accessible from the Dashboard with 1 tap via the **"Brew"** action or the new **Smart Pour Card**.
2. **Full Currency Switch to Indian Rupee (₹ INR)**:
   - Replaced all dollar (`$`) symbols across:
     - `DashboardScreen.kt`: Total spend card now displays `₹`.
     - `AddEditVisitScreen.kt`: Ordered items, spend summary box, and rating ratio now show `₹` and `pts/₹`.
     - `VisitDetailScreen.kt`: Visit item prices and ratio formatted in `₹`.
     - `SensoryAnalyticsScreen.kt`: Monthly coffee budget pulse in `₹`.
     - `DataExporter.kt`: CSV export formatted with `₹`.
3. **Specialty Coffee Spend & Monthly Budget Pulse** (`CoffeeSpendTracker.kt`):
   - Added to `SensoryAnalyticsScreen.kt`.
   - Tracks monthly spend against ₹3,500 budget with progress bar and home brewing savings (+₹820 saved).
4. **Passport Gamification & Badges** (`PassportBadge.kt` & `PassportBadgeCatalog`):
   - Integrated with user milestones across total visits, origins explored, and cupping scores.

---

## 🤖 Android Studio Master Prompt (Copy & Paste)

If you are using **Android Studio Gemini**, **GitHub Copilot**, or any coding assistant inside Android Studio, paste the following prompt:

```text
You are assisting me on "AfterTaste", an offline-first Specialty Coffee Journal & Cafe Passport app built with Kotlin 2.2 and Jetpack Compose (Material 3).

Please verify and enforce the following v2.0.0 architecture standards across all screens:
1. CURRENCY: All monetary figures and budget calculations must use the Indian Rupee symbol (₹). Under no circumstances should the dollar sign ($) be displayed.
2. BREW TIMER & GUIDE: Ensure `BrewTimerDialog` (located in `com.aftertaste.ui.components.BrewTimerDialog`) is wired to `DashboardScreen.kt` via the "Brew" action in `QuickActionBar` and the "Smart Pour Timer & Ratios" banner card.
3. SPEND TRACKER: Ensure `SensoryAnalyticsScreen.kt` renders the `CoffeeSpendTracker` summary card showing monthly budget progress (₹), cafe hopping spend, vault beans spend, and estimated home savings.
4. MATERIAL 3 DESIGN: Follow the coffee color palette (CoffeeClay #2C1810, EspressoText #1A0F0A, ParchmentCream #FDFBF7, TerracottaAccent #D97706) and rounded corners (20.dp).
5. LOCAL DATABASE: Ensure Room 2.7.0 queries in `CafeRepositoryImpl.kt` remain strictly offline-first.

Please inspect my current build, run Gradle Sync, and make sure `assembleDebug` succeeds with 0 warnings or errors.
```

---

## 🛠️ Step-by-Step Android Studio Build Instructions

Follow these steps inside **Android Studio**:

### Step 1: Sync Gradle
1. Open the project in Android Studio: `c:\Users\Sukhansh Mittal\AndroidStudioProjects\AfterTaste`.
2. Click **File** ➔ **Sync Project with Gradle Files** (or click the Elephant icon with the blue arrow in the top-right toolbar).
3. Wait for the build window to finish indexing.

### Step 2: Invalidate Caches (To Clear Stale APKs)
If Android Studio previously installed an older build onto your phone or emulator:
1. Go to **File** ➔ **Invalidate Caches...**
2. Check all options (including *Clear file system cache and Local History*).
3. Click **Invalidate and Restart**.

### Step 3: Run / Install
1. Connect your physical Android phone (with USB Debugging enabled) or start an Android Virtual Device (AVD).
2. Press **Shift + F10** or click the green **▶ Run 'app'** button in the top toolbar.
3. Alternatively, install the freshly built APK via terminal:
   ```powershell
   adb install -r c:\Users\Sukhansh Mittal\AndroidStudioProjects\AfterTaste\app\build\outputs\apk\debug\app-debug.apk
   ```

---

## 📂 Architecture & File Reference

| File | Location | Changes in v2.0.0 |
| :--- | :--- | :--- |
| **`BrewTimerDialog.kt`** | `app/src/main/java/com/aftertaste/ui/components/` | **NEW**: Live countdown pour timer, stage progression (Bloom, Pours, Drawdown), ratio calculator. |
| **`DashboardScreen.kt`** | `app/src/main/java/com/aftertaste/ui/dashboard/` | Added "Brew" quick action button, "Smart Pour Timer" banner card, and ₹ currency format. |
| **`SensoryAnalyticsScreen.kt`** | `app/src/main/java/com/aftertaste/ui/analytics/` | Added Specialty Coffee Spend & Budget Card (₹ INR) with home-brew savings breakdown. |
| **`AddEditVisitScreen.kt`** | `app/src/main/java/com/aftertaste/ui/visit/` | Updated item prices, summary box, and rating ratios to ₹ INR (`pts/₹`). |
| **`VisitDetailScreen.kt`** | `app/src/main/java/com/aftertaste/ui/visit/` | Updated visit prices and price-to-quality ratio to ₹ INR. |
| **`DataExporter.kt`** | `app/src/main/java/com/aftertaste/util/` | Formatted exported CSV items with ₹ currency. |
| **`BrewGuideHelper.kt`** | `app/src/main/java/com/aftertaste/util/` | Mathematical ratios and stages for V60, AeroPress, French Press, and Moka Pot. |
| **`CoffeeSpendTracker.kt`** | `app/src/main/java/com/aftertaste/util/` | Spending calculations and budget pulse engine. |
| **`PassportBadge.kt`** | `app/src/main/java/com/aftertaste/data/local/entity/` | Digital coffee badge definitions and milestone unlocks. |

---

## 🧪 Verification Checklist

- [x] **Dashboard Currency**: Total spend card reads `₹...` instead of `$`.
- [x] **Brew Timer Action**: Tapping the "Brew" icon or "Smart Pour Timer" card opens the interactive timer dialog.
- [x] **Brew Timer Controls**: Starting the timer runs the countdown; skipping moves to the next pour stage; dose adjustments (`-1g` / `+1g`) update the water requirement automatically.
- [x] **Analytics Budget Pulse**: Navigating to Analytics displays the Coffee Spend & Budget card in ₹ INR.
- [x] **Add Visit Prices**: Adding an item displays `Price (₹)` and computes total spend in `₹`.
- [x] **Build Status**: Compiles cleanly with Gradle 8.11.1 and Kotlin 2.2.0.
