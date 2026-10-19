---
version: alpha
name: Ohryu TV Design System
description: Ultra-clean, anti-AI-slop 10-foot design system for lightweight Android TV interfaces using the signature Ohryu palette.
colors:
  primary: "#CEE800"
  secondary: "#765BB7"
  tertiary: "#FF8F1C"
  accent-pink: "#F55393"
  accent-violet: "#8626C3"
  background: "#0B0B0F"
  surface: "#141620"
  surface-hover: "#1F2333"
  text-primary: "#F8FAFC"
  text-muted: "#94A3B8"
  text-dark: "#0B0B0F"
  border-subtle: "#2A2E42"
  border-focused: "#CEE800"
typography:
  display:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "3.5rem"
    fontWeight: 800
    lineHeight: 1.1
    letterSpacing: "-0.03em"
  h1:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "2rem"
    fontWeight: 700
    lineHeight: 1.2
    letterSpacing: "-0.02em"
  h2:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "1.5rem"
    fontWeight: 600
    lineHeight: 1.3
  body-lg:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "1.125rem"
    fontWeight: 500
    lineHeight: 1.4
  body-md:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "1rem"
    fontWeight: 400
    lineHeight: 1.5
  label:
    fontFamily: Inter, Roboto, sans-serif
    fontSize: "0.875rem"
    fontWeight: 600
    lineHeight: 1.2
    letterSpacing: "0.04em"
rounded:
  sm: "8px"
  md: "14px"
  lg: "20px"
  pill: "999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "36px"
  tv-safe-h: "56px"
  tv-safe-v: "40px"
components:
  app-card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.md}"
    padding: "16px"
    width: "196px"
    height: "172px"
  app-card-focused:
    backgroundColor: "{colors.surface-hover}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.md}"
    padding: "16px"
    width: "196px"
    height: "172px"
  action-badge-lime:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.text-dark}"
    rounded: "{rounded.pill}"
    padding: "8px"
  category-tab-active:
    backgroundColor: "{colors.secondary}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.pill}"
    padding: "12px"
  menu-item-focused:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.text-dark}"
    rounded: "{rounded.sm}"
    padding: "12px"
  status-badge-pink:
    backgroundColor: "{colors.accent-pink}"
    textColor: "{colors.text-dark}"
    rounded: "{rounded.pill}"
    padding: "6px"
  dialog-header-violet:
    backgroundColor: "{colors.accent-violet}"
    textColor: "{colors.text-primary}"
    rounded: "{rounded.sm}"
    padding: "12px"
  app-label-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.text-muted}"
    rounded: "{rounded.sm}"
    padding: "4px"
  focus-ring-indicator:
    backgroundColor: "{colors.border-focused}"
    textColor: "{colors.text-dark}"
    rounded: "{rounded.sm}"
    padding: "2px"
  subtle-border-indicator:
    backgroundColor: "{colors.border-subtle}"
    textColor: "{colors.text-muted}"
    rounded: "{rounded.sm}"
    padding: "2px"
---

## Overview

Ohryu TV Design System dibangun khusus untuk antarmuka televisi 10-foot (jarak pandang 3 meter) yang menolak estetika "AI Slop" — tidak ada gradient ungu generik yang kabur, tidak ada drop-shadow berlapis-lapis yang mencekik frame rate GPU, dan tidak ada ruang kosong raksasa tanpa tujuan.

Gaya ini memadukan kedalaman kanvas gelap pekat (`#0B0B0F`) dengan **Voltage Lime (`#CEE800`)** sebagai satu-satunya penanda navigasi D-pad remote yang tegas, instan, dan terbaca jelas dalam pandangan sekilas.

## Colors

Palet resmi Ohryu identitas:
- **Primary / Voltage Lime (`#CEE800`):** Kursor fokus aktif utama remote control. Begitu pengguna memencet D-pad, item yang dituju langsung berpendar dengan warna ini.
- **Secondary / Deep Amethyst (`#765BB7`):** Penanda kategori aktif, header baris favorit, dan aksen navigasi sekunder.
- **Background / Abyss (`#0B0B0F`):** Latar belakang 100% hitam pekat yang mengistirahatkan mata dan ramah konsumsi daya TV panel OLED/LED.
- **Surface (`#141620`):** Latar kartu aplikasi normal (tidak sedang disorot).
- **Surface Hover (`#1F2333`):** Latar kartu aplikasi saat sedang difokuskan.
- **Accent Pink (`#F55393`):** Badge notifikasi penting atau status update.
- **Accent Orange (`#FF8F1C`):** Badge aplikasi favorit (pin icon / label).
- **Accent Violet (`#8626C3`):** Pembatas dialog dan bayangan aksen.

## Typography

Seluruh skala tipografi dirancang agar terbaca tajam dari jarak 3 meter tanpa membuat mata tegang:
- **Display (3.5rem / 56px, ExtraBold):** Widget jam beranda digital di pojok kanan/kiri atas.
- **H1 (2rem / 32px, Bold):** Judul seksi kategori ("Aplikasi Favorit", "Semua Aplikasi").
- **H2 (1.5rem / 24px, SemiBold):** Judul dialog aksi aplikasi (Pin/Hide/Info).
- **Body-LG (1.125rem / 18px, Medium):** Label nama aplikasi di bawah icon.
- **Label (0.875rem / 14px, Bold, Uppercase):** Tag status sistem (Wi-Fi, Status Memori, Indikator TV).

## Layout

1. **TV Safe Area (Overscan Protection):**
   - Margin horizontal kiri & kanan wajib minimal `56px` (`spacing.tv-safe-h`).
   - Margin vertikal atas & bawah wajib minimal `40px` (`spacing.tv-safe-v`).
   - Tidak ada elemen interaktif yang boleh menempel ke bibir layar terluar.
2. **Grid Spacing:**
   - Grid 5 kolom horizontal dengan jarak celah konsisten `16px` (`spacing.md`).
   - Rasio kartu icon aplikasi 196dp × 172dp, menjamin proporsi icon 80dp × 80dp di tengah dan label teks 1 baris terpusat di bawah.

## Elevation & Depth

TV Sharp memiliki keterbatasan pengolahan shader grafis. Oleh karena itu:
- **Level 0 (Kanvas Dasar):** `#0B0B0F` murni tanpa gradien rumit.
- **Level 1 (Card Normal):** Flat `#141620` dengan border tipis 1dp `#2A2E42`.
- **Level 2 (Card Focused):** Micro-scale `1.06x` + border solid 2dp Voltage Lime `#CEE800` + glow terarah 8dp `#CEE800` (opacity 35%). Terlihat melompat ke depan tanpa memakan resource GPU.
- **Level 3 (Modal Dialog):** `#141620` pekat di atas backdrop semi-transparan `rgba(11, 11, 15, 0.85)`.

## Shapes

- Radius standar kartu aplikasi adalah `14px` (`rounded.md`), memberikan lekukan modern yang halus namun tetap kokoh.
- Tombol aksi dialog dan badge menggunakan `rounded.sm` (`8px`) atau `rounded.pill` (`999px`).

## Components

1. **`app-card` (Default):**
   Wadah icon aplikasi dalam keadaan idle. Latar belakang `#141620`, border halus `#2A2E42`.
2. **`app-card-focused` (D-pad Focus):**
   Wadah icon saat disorot remote. Membesar `1.06x`, border menyala `#CEE800`.
3. **`category-tab-active`:**
   Pill filter kategori aplikasi dengan latar `#765BB7` dan teks putih terang.
4. **`action-badge-lime`:**
   Chip status/indikator dengan warna `#CEE800` kontras tinggi terhadap teks gelap `#0B0B0F`.

## Do's and Don'ts

### Do's:
- **Wajib** menjaga rasio kontras kursor remote di atas 10:1 terhadap latar belakang (Voltage Lime pada Abyss memenuhi WCAG AAA).
- **Wajib** memberikan feedback visual instan saat tombol D-pad ditekan (perubahan warna border seketika < 16ms).
- **Wajib** membatasi label teks aplikasi maksimal 1 baris dengan truncation `android:ellipsize="end"`.

### Don'ts:
- **Jangan** menggunakan efek glassmorphism blur berat di TV (bikin chipset MediaTek drop frame ke 15 FPS).
- **Jangan** menggunakan warna abu-abu samar yang sulit dibedakan dari jarak 3 meter.
- **Jangan** menaruh tombol penting di luar area TV safe overscan.
- **Jangan** menggunakan animasi transisi halaman yang lebih lama dari 150ms.
