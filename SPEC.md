# Technical Specification (SPEC.md)
## Project: Ohryu TV Launcher (Gwolf TV)
**Tech Stack:** Kotlin 1.9+ | Android SDK 26–34 | AndroidX ViewBinding | ProGuard / R8 Full Mode  
**Architecture:** Single-Activity | MVVM (Lightweight) | Zero-3rd-Party-Bloat  
**Target Output:** Release APK ≤ 2.5 MB  

---

## 1. Architectural Decisions

### 1.1. Native Views (XML + RecyclerView) over Jetpack Compose
- **Decision:** Menggunakan **Native XML Layouts + ViewBinding + RecyclerView**, BUKAN Jetpack Compose for TV.
- **Rationale:** Jetpack Compose membutuhkan runtime reflection dan bytecode overhead (~15MB APK, recomposition latency di GPU Mali-450 / Mali-G31 pada chipset Sharp TV). Native Views menjamin render 60 FPS konsisten dengan memory footprint < 20MB.

### 1.2. Zero Heavy Third-Party Libraries
- **Allowed:** `androidx.appcompat`, `androidx.recyclerview`, `androidx.core:core-ktx`, `com.google.android.material:material` (lightweight components only).
- **Prohibited:** Dagger/Hilt, Koin, Room DB, Retrofit, Firebase, Glide/Coil (icon app lokal cukup di-load langsung via `PackageManager.loadIcon()` dengan in-memory LRU cache ringan).

---

## 2. Project Directory Structure

```text
gwolf-tv-launcher/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/gwolf/tvlauncher/
│           │   ├── data/
│           │   │   ├── AppModel.kt
│           │   │   ├── AppRepository.kt
│           │   │   └── LauncherPreferences.kt
│           │   ├── ui/
│           │   │   ├── MainActivity.kt
│           │   │   ├── MainViewModel.kt
│           │   │   ├── adapter/
│           │   │   │   ├── AppGridAdapter.kt
│           │   │   │   └── AppItemDiffCallback.kt
│           │   │   ├── dialog/
│           │   │   │   └── AppActionBottomSheet.kt
│           │   │   └── view/
│           │   │       └── DpadFocusableCard.kt
│           │   └── util/
│           │       ├── TvKeyHandler.kt
│           │       └── PackageReceiver.kt
│           └── res/
│               ├── drawable/
│               │   ├── bg_card_focus.xml
│               │   ├── bg_card_normal.xml
│               │   └── ic_launcher_banner.xml
│               ├── layout/
│               │   ├── activity_main.xml
│               │   ├── item_app_card.xml
│               │   └── dialog_app_actions.xml
│               └── values/
│                   ├── colors.xml (Ohryu Tokens)
│                   ├── dimens.xml
│                   └── styles.xml
├── build.gradle.kts
├── settings.gradle.kts
├── PRD.md
├── SPEC.md
└── DESIGN.md
```

---

## 3. Component Specifications

### 3.1. AndroidManifest.xml Contract
Wajib mendeklarasikan intent filter kategori Leanback Launcher dan Android TV hardware features:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gwolf.tvlauncher">

    <uses-feature
        android:name="android.software.leanback"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.touchscreen"
        android:required="false" />

    <!-- Mengizinkan query paket pada Android 11+ (API 30+) -->
    <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />

    <application
        android:allowBackup="true"
        android:banner="@drawable/ic_launcher_banner"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.OhryuTV.NoActionBar">

        <activity
            android:name=".ui.MainActivity"
            android:banner="@drawable/ic_launcher_banner"
            android:configChanges="keyboard|keyboardHidden|navigation|orientation|screenSize|screenLayout"
            android:exported="true"
            android:launchMode="singleTask"
            android:screenOrientation="landscape"
            android:stateNotNeeded="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

### 3.2. Data Models & Repository

#### `AppModel.kt`
```kotlin
data class AppModel(
    val packageName: String,
    val activityName: String,
    val label: String,
    val isLeanback: Boolean,
    val isFavorite: Boolean = false,
    val isHidden: Boolean = false,
    val customOrder: Int = 0
)
```

#### `AppRepository.kt`
- **Query Package Manager:**
  ```kotlin
  fun loadInstalledApps(): List<AppModel> {
      val pm = context.packageManager
      val tvIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
      val mobileIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

      val tvApps = pm.queryIntentActivities(tvIntent, 0)
      val mobileApps = pm.queryIntentActivities(mobileIntent, 0)

      val map = mutableMapOf<String, AppModel>()
      // Prioritaskan leanback activity, fallback ke mobile activity
      for (ri in tvApps) {
          val pkg = ri.activityInfo.packageName
          if (pkg == context.packageName) continue
          map[pkg] = AppModel(
              packageName = pkg,
              activityName = ri.activityInfo.name,
              label = ri.loadLabel(pm).toString(),
              isLeanback = true
          )
      }
      for (ri in mobileApps) {
          val pkg = ri.activityInfo.packageName
          if (pkg == context.packageName || map.containsKey(pkg)) continue
          map[pkg] = AppModel(
              packageName = pkg,
              activityName = ri.activityInfo.name,
              label = ri.loadLabel(pm).toString(),
              isLeanback = false
          )
      }
      return map.values.sortedBy { it.label.lowercase() }
  }
  ```

### 3.3. D-pad & 10-Foot Focus Handling

#### `DpadFocusableCard.kt`
Kustom ViewGroup atau View item card dengan:
- `isFocusable = true` dan `isFocusableInTouchMode = true`
- `stateListAnimator` yang memicu skala micro-zoom (`scaleX = 1.06f, scaleY = 1.06f`) dengan transisi durasi 120ms saat `isFocused = true`.
- Menggambar border bercahaya Lime `#CEE800` (2dp solid + 6dp soft glow) saat difokuskan.

#### Key Event Interception di `MainActivity.kt`:
- **Tombol Back (`KEYCODE_BACK`):**
  - Jika dialog/modal sedang terbuka ➔ tutup dialog.
  - Jika berada di grid aplikasi ➔ arahkan fokus kembali ke item pertama baris favorit (tidak boleh keluar dari launcher).
- **Tombol Menu (`KEYCODE_MENU`):**
  - Memicu dialog *App Actions* pada item yang sedang dalam status `isFocused`.
- **Long Press Center / Enter (`KEYCODE_DPAD_CENTER`):**
  - Mengaktifkan `onLongClickListener` untuk memunculkan aksi Pin/Hide/Info.

---

## 4. Storage & Persistence (`LauncherPreferences.kt`)
Penyimpanan setelan menggunakan `SharedPreferences` privat:
- `KEY_PINNED_APPS`: Set `<String>` berisi package name yang di-pin ke baris favorit.
- `KEY_HIDDEN_APPS`: Set `<String>` berisi package name yang disembunyikan.
- `KEY_GRID_COLUMNS`: Integer jumlah kolom (default: `5`).
- Total file preferensi < 5 KB.

---

## 5. ProGuard / R8 Aggressive Optimization
```proguard
# Optimasi performa dan ukuran biner
-repackageclasses 'com.gwolf.tv'
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively

# Strip semua logging debug pada release APK
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Keep essentials
-keep class com.gwolf.tvlauncher.ui.MainActivity
```

---

## 6. Build Target & Validation
- **Minification:** `isMinifyEnabled = true` dan `isShrinkResources = true`.
- **Target APK Size:** 1.2 MB – 2.0 MB.
- **Memory Target on Sharp TV:** Heap Allocation idle ~14 MB, PSS total ~22 MB.
