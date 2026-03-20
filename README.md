# ThreadStyle

**ThreadStyle** is an Android app for styling text using Unicode character substitution — perfect for social media posts, bios, and anywhere plain text is too plain.

Type or paste text, select it, and tap a style button to transform it instantly.

---

## Screenshots

| Main | Settings | About |
|------|----------|-------|
| ![Main](screenshots/main.png) | ![Settings](screenshots/settings.png) | ![About](screenshots/about.png) |

---

## What it does

ThreadStyle lets you type (or paste) text, select a portion of it, and transform it into a styled variant using a tap on one of the style buttons. The full composer text is then copied to the clipboard with one tap.

Styled text is made of real Unicode characters — no images, no fonts — so it pastes correctly into Twitter/X threads, Instagram bios, LinkedIn posts, Discord, and anywhere else that accepts plain text.

---

## Styles

| Button | Example |
|---|---|
| italic | *hello* |
| bold italic | ***hello*** |
| bold cursive | 𝓱𝓮𝓵𝓵𝓸 |
| doublestruck | 𝕙𝕖𝕝𝕝𝕠 |
| redacted | ████ |
| small caps | ʜᴇʟʟᴏ |
| mono | hello (monospace) |
| witchy / bold witchy | 𝔥𝔢𝔩𝔩𝔬 / 𝖍𝖊𝖑𝖑𝖔 |
| script / bold script | 𝒽𝑒𝓁𝓁ℴ / 𝓱𝓮𝓵𝓵𝓸 |
| sans / sans bold / sans italic | 𝗁𝖾𝗅𝗅𝗈 variants |
| circled | ⓗⓔⓛⓛⓞ |
| wide | ｈｅｌｌｏ |
| flip | oןןǝɥ |
| 🅱ubble | bubble letters |
| underline | h̲e̲l̲l̲o̲ |
| strike | h̶e̶l̶l̶o̶ |
| sup | ʰᵉˡˡᵒ |
| asian | aesthetic spaced |
| thai | ฺhello |
| 👏 clap | word👏by👏word |
| sArCaSm | hElLo |
| wizard | rune-style |

Multiple styles can be applied in sequence — each transform reverses any previous styling first, so you always start from clean text.

---

## Features

- **No internet required** — fully offline
- **No special permissions** — clipboard read/write only
- **⎘ Copy** — copies styled text to clipboard with optional C5–E5–G5 chord tone
- **Copy tone** — toggle audio feedback in Settings
- **Copy History** — tap any past entry to re-copy it
- **Character counter** — live 0 / 500 counter in the composer
- **Load from clipboard** — paste clipboard contents into the composer for further editing

---

## Requirements

- Android 7.0 (API 24) or higher
- No external dependencies beyond AndroidX and Material Components

---

## Building

```bash
git clone https://github.com/tekphreak/threadstyle.git
cd threadstyle
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Project structure

```
app/src/main/
├── java/com/tekphreak/threadstyle/
│   ├── MainActivity.kt        # Composer, style buttons, copy/load logic
│   ├── SettingsActivity.kt    # Copy tone toggle + copy history
│   ├── AboutActivity.kt       # App icon, version, links, C5-E5-G5 on open
│   ├── StylerEditText.kt      # Selection-preserving EditText subclass
│   └── CharMaps.kt            # All Unicode character maps + reverse map
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_settings.xml
    │   └── activity_about.xml
    └── drawable/
        ├── ic_bookmark.xml
        ├── ic_settings.xml
        └── btn_style_bg.xml
```

---

## Support

If you find ThreadStyle useful, consider buying me a coffee:

[![Ko-fi](https://img.shields.io/badge/Donate-Ko--fi-ff5e5b?logo=ko-fi)](https://ko-fi.com/tekphreak)

[tekphreak.com](https://tekphreak.com)

---

## License

MIT
