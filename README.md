# Ohryu TV Launcher (Gwolf TV)

> Ultra-lightweight, bloat-free, zero-telemetry Android TV launcher designed for low-memory Smart TVs (Sharp Android TV / Google TV).

---

## ⚡ Highlights

- **Ultra-lightweight:** Idle RAM target < 25 MB (Google TV uses 350+ MB).
- **Fast & Responsive:** 60 FPS D-pad 10-foot navigation with < 16ms latency.
- **Pure Local:** 0 network requests, 0 telemetry, 0 ads, 100% offline.
- **Bespoke Design:** Crafted using the signature **Ohryu Palette** (Dark `#0B0B0F` canvas with high-voltage Lime `#CEE800` D-pad focus cursor).
- **Native Stack:** Built with pure Kotlin, AndroidX Native Views (RecyclerView + ViewBinding), and aggressive ProGuard/R8 shrinking. No heavy Compose or 3rd-party runtimes.

---

## 📚 Architecture & Documentation

- **[PRD.md](PRD.md)**: Product Requirements Document, core KPIs, and feature breakdown.
- **[SPEC.md](SPEC.md)**: Technical Architecture Specification, Native View contracts, and key event interception.
- **[DESIGN.md](DESIGN.md)**: Google-compliant DESIGN.md spec containing design tokens, Ohryu color palette, typography scales, TV safe area overscan, and WCAG AAA contrast compliance.

---

## 🛠️ Project Structure

```text
gwolf-tv-launcher/
├── app/                  # Android application module (Kotlin)
├── PRD.md                # Product Requirements Document
├── SPEC.md               # Technical Architecture Specification
└── DESIGN.md             # Formal Design System & Tokens
```

---

## 📄 License

MIT © [Muhammad Azif Zain Rizky](https://github.com/AzifZR)
