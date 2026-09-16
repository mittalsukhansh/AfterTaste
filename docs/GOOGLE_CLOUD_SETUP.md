# Google Cloud Console Setup Guide for AfterTaste ☕

This guide walks you through enabling **Google Maps SDK for Android** and **Places API**, generating an API key, restricting it securely to the `com.aftertaste` package and SHA-1 fingerprint, and configuring it in your project.

---

## 🛠 Step 1: Create a Google Cloud Project

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Log in with your Google Account.
3. Click the project dropdown at the top of the page and select **New Project**.
4. Name the project **AfterTaste** and click **Create**.

---

## 📍 Step 2: Enable Required APIs

1. In the Google Cloud Console, open the left navigation menu and go to **APIs & Services > Library**.
2. Search for **Maps SDK for Android** and click **Enable**.
3. Search for **Places API** (or **Places API (New)**) and click **Enable**.
4. Make sure billing is enabled for your project (Google Cloud offers $200 in free monthly credits for Maps/Places).

---

## 🔑 Step 3: Generate and Restrict Your API Key

1. Go to **APIs & Services > Credentials**.
2. Click **Create Credentials** at the top and choose **API key**.
3. Copy the generated API key.
4. Click **Edit API key** (or click the key name) to restrict it securely:

### Application Restrictions:
- Select **Android apps**.
- Click **Add an item**:
  - **Package name**: `com.aftertaste`
  - **SHA-1 certificate fingerprint**: Get your SHA-1 key by running this command in Android Studio Terminal:
    ```bash
    ./gradlew signingReport
    ```
    Copy the `SHA1` fingerprint output for `variant: debug` (e.g. `AA:BB:CC:11:22:33:...`).

### API Restrictions:
- Select **Restrict key**.
- Under **Select APIs**, check:
  - **Maps SDK for Android**
  - **Places API**
- Click **Save**.

---

## ⚙️ Step 4: Add API Key to local.properties

Open `local.properties` in the root directory of your Android project and add your key:

```properties
sdk.dir=C\:\\Users\\Sukhansh Mittal\\AppData\\Local\\Android\\Sdk
MAPS_API_KEY=AIzaSyYourActualKeyGeneratedAbove
```

> [!IMPORTANT]
> **Security Note**: `local.properties` is included in `.gitignore` and is never committed to Git, keeping your API key safe.

---

## 🚀 Step 5: Verify on Device / Emulator

1. Clean and rebuild the project in Android Studio:
   ```bash
   ./gradlew clean app:assembleDebug
   ```
2. Run the app on your phone or emulator with Location/GPS enabled.
3. Open the **Map** tab:
   - Google Maps will render centered at your GPS location.
   - Real nearby cafes fetched from Places API will appear as markers on the map!
