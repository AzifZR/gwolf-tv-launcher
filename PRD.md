# Product Requirements Document (PRD)
## Project: Ohryu TV Launcher (Gwolf TV)
**Version:** 1.0.0  
**Target Platform:** Android TV / Google TV (Android 9.0 Pie / API 28 up to Android 14 / API 34)  
**Primary Target Device:** Sharp Smart TV (1.5GB - 2GB RAM, MediaTek / Realtek SoC)  
**Author:** Muhammad Azif Zain Rizky & Hermes Pribadi  
**Status:** Approved for Implementation by OpenCode  

---

## 1. Problem Statement
Sharp Smart TV dan perangkat Android TV kelas menengah-bawah mengalami degradasi performa parah karena:
1. **Google TV Launcher Bloatware:** Mengalokasikan 250MB–450MB RAM hanya untuk merender feed video trailer otomatis, banner iklan bersponsor, dan service analitik latar belakang.
2. **D-pad Remote Latency:** Responsivitas remote TV mengalami lag 2–5 detik akibat GPU & CPU tercekik proses background launcher bawaan.
3. **Visual Clutter & AI Slop:** Tampilan penuh distraksi konten yang tidak pernah ditonton oleh pengguna, menenggelamkan aplikasi utama (YouTube, Netflix, SmartTube, Vidio, Browser).

## 2. Product Vision & Value Proposition
**Ohryu TV Launcher** adalah launcher Android TV ultra-ringan, berkecepatan instan, dan 100% bebas iklan/telemetri dengan bahasa desain khas **Ohryu Palette** (Dark `#0B0B0F`, Lime `#CEE800`, Purple `#765BB7`). 

### Core KPIs & Performance Budget
| Metrik | Target Maksimal | Baseline Google TV Launcher |
|---|---|---|
| **RAM Footprint (Idle)** | **< 25 MB** | 320 MB - 450 MB |
| **RAM Footprint (Active)** | **< 40 MB** | 500 MB+ |
| **Ukuran APK Final** | **< 3.5 MB** | ~60 MB |
| **Cold Start Time** | **< 350 ms** | 2.500 ms - 4.000 ms |
| **D-pad Input Latency** | **< 16 ms (1 frame @ 60fps)** | 150 ms - 800 ms |
| **Network Requests** | **0 Request (100% Offline)** | Puluhan request per menit |

---

## 3. Target Environment & 10-Foot UI Principles
- **Viewing Distance:** 2.5 – 4.0 meter dari layar televisi (resolusi Full HD 1080p & 4K UHD).
- **Input Method:** Remote control D-pad (Up, Down, Left, Right, Center/OK, Back, Home, Menu). Tidak ada touchscreen atau mouse cursor.
- **TV Safe Area (Overscan):** Margin aman minimal 48dp (5% horizontal & vertical margin) agar konten tidak terpotong bezel TV tabung/panel lama.
- **High-Voltage Focus States:** Pengguna harus tahu posisi kursor aktif dalam waktu < 0.1 detik tanpa berpikir (didukung aksen Lime `#CEE800` bercahaya tajam).

---

## 4. Feature Specifications

### 4.1. F1: Home Grid & App Launching (Core)
- **Auto-Discovery:** Membaca semua aplikasi terpasang melalui `Intent.CATEGORY_LEANBACK_LAUNCHER` dengan fallback ke `Intent.CATEGORY_LAUNCHER`.
- **Layout:** Grid 5 kolom (1080p) atau 6 kolom (4K) yang proporsional dan tidak berantakan.
- **Card State:** Tiap card memuat icon app resolusi tinggi (di-cache di memori) + label teks ringkas di bawahnya.
- **Zero-Lag Click:** Menekan tombol OK/Enter langsung meluncurkan intent aplikasi target tanpa transisi animasi yang memakan beban GPU.

### 4.2. F2: Pinning, Sorting & Favorites Row
- **Baris Favorit (Top Row):** Baris teratas dialokasikan untuk 4–6 aplikasi yang paling sering digunakan.
- **Manual Reorder:** Pengguna bisa menggeser urutan aplikasi ke kiri/kanan.
- **Persistence:** Konfigurasi urutan tersimpan secara lokal via SharedPreferences / DataStore primitif (zero database engine overhead).

### 4.3. F3: App Management & Hide Unwanted Apps
- **Context Menu (Long Press OK / Tombol Menu):** Menekan lama tombol OK pada aplikasi memunculkan dialog ringkas:
  1. *Pin ke Favorit / Unpin*
  2. *Sembunyikan Aplikasi (Hide from Drawer)*
  3. *Buka Info Aplikasi (App Info)*
  4. *Uninstall Aplikasi*
- **Hidden Apps Manager:** Menu tersembunyi di pojok kanan atas untuk memulihkan aplikasi yang disembunyikan.

### 4.4. F4: Minimal Header Bar (Zero Telemetry)
- **Jam & Tanggal Lokal:** Format jam besar yang mudah dibaca (`20:45` WIB, `Senin, 19 Okt`) tanpa polling internet (menggunakan broadcast receiver OS).
- **Status Jaringan & Cepat:** Indikator kecil icon Wi-Fi / LAN Ethernet (hanya menampilkan status koneksi perangkat lokal).
- **Settings Shortcut:** Tombol cepat langsung membuka Settings OS Android TV bawaan (`android.settings.SETTINGS`).

### 4.5. F5: Pure Black Wallpaper & Low-Spec Mode
- **OLED / Pure Black Theme:** Background default `#0B0B0F` pekat untuk efisiensi daya panel OLED dan minimalisasi rendering pipeline GPU TV Sharp.
- **Custom Backdrop (Optional):** Kemampuan memilih wallpaper statis lokal tanpa animasi dinamis.

---

## 5. Non-Goals (Hal yang Dilarang Dibuat)
1. **DILARANG** menambahkan modul iklan, banner bersponsor, atau tracking SDK apa pun (Firebase Analytics, AdMob, Google Play Services dependensi berat).
2. **DILARANG** menambahkan feed video scraping atau rekomendasi konten online (YouTube feed, trailer stream) di halaman beranda.
3. **DILARANG** membuat animasi transisi bertingkat/blur berat (RenderScript blur / dynamic blur) yang membuat GPU TV patah-patah.
4. **DILARANG** mewajibkan login akun atau sinkronisasi cloud. Semua data hidup dan mati di memori lokal TV.

---

## 6. Release Roadmap & Milestones
- **M1 (Core Prototype):** Android Project skeleton, Leanback/RecyclerView 5-kolom, remote navigation D-pad lancar, icon extractor.
- **M2 (Management & Polish):** Long-press action dialog (Pin, Hide, App Info), settings launcher shortcut, Ohryu theme token integration.
- **M3 (Build & Deployment):** Release APK signed, debloat automation script untuk TV Sharp via ADB.
