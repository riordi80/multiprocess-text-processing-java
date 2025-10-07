# UT1 – Multiprocess Text Processing (Java)

A small Java project where a **parent** process launches one **child** per input file (`datosX.txt`). Each child lowercases words, counts vowels (including accented vowels and `ü`), writes partial results to `.res` files, and the parent prints a per-file report.

---

## Features
- One child per file (`ProcessBuilder` + `waitFor()`)
- Lowercase normalization (one word per line)
- Vowel counting: `a e i o u á é í ó ú ü`
- Ignores empty lines (not counted as words)
- UTF-8 I/O
- Outputs per file:
  - `minusculas-X.res` — lowercase words
  - `vocales-X.res` — total vowels
  - `palabras-X.res` — total words
- Parent report: words, vowels, avg vowels/word

---

## Structure
```
.
├─ src/actividad_ut1/
│  ├─ Main.java       # Parent: spawns child, waits, aggregates
│  └─ Contador.java   # Child: processes one file, writes .res
├─ data/              # optional: sample datos1.txt … datos4.txt
├─ docs/              # optional: UML/report
└─ README.md
```

---

## Requirements
- JDK 17+ (recommended)

---

## Build
```bash
# From repo root
mkdir -p out
javac -encoding UTF-8 -d out src/actividad_ut1/*.java
```

## Run
> Adjust the base path in `Main.java` to where your `datosX.txt` files are stored.
```bash
java -cp out actividad_ut1.Main
```
**Output** (example):
```
=== Report datos1.txt ===
Words processed: 120
Total vowels:    310
Avg vowels/word: 2.583
```
**Generated files** (next to inputs): `minusculas-1.res`, `vocales-1.res`, `palabras-1.res`, …

---

## Notes
- Average handles division by zero (empty files → 0.0)
- UTF-8 ensures accents/`ü` work as expected
- Empty lines are skipped

