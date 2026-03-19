# ThreadStyle

An Android app for styling text using Unicode mathematical and decorative character sets — then copying the result straight to your clipboard, ready to paste anywhere.

![ThreadStyle Logo](threadstyle-logo.png)

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
| redacted | h̶e̶l̶l̶o̶ |
| small caps | ʜᴇʟʟᴏ |
| mono | ｈｅｌｌｏ |
| witchy / bold witchy | 𝔥𝔢𝔩𝔩𝔬 / 𝖍𝖊𝖑𝖑𝖔 |
| script / bold script | 𝒽𝑒𝓁𝓁ℴ / 𝓱𝓮𝓵𝓵𝓸 |
| sans / sans bold / sans italic | 𝗁𝖾𝗅𝗅𝗈 variants |
| circled | ⓗⓔⓛⓛⓞ |
| wide | ｈｅｌｌｏ |
| flip | oןןǝɥ |
| 🅱ubble | 🅗🅔🅛🅛🅞 |
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
- **Copy tone** — optional ascending C5–E5–G5 chord on successful copy (can be toggled in Settings)
- **Copy History** — Settings screen shows a scrollable list of everything you've copied; tap any entry to re-copy it
- **Character counter** — live 0 / 500 counter in the composer
- **Load from clipboard** — paste your clipboard contents back into the composer for further editing

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
│   ├── StylerEditText.kt      # Selection-preserving EditText subclass
│   └── CharMaps.kt            # All Unicode character maps + reverse map
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   └── activity_settings.xml
    └── drawable/
        ├── ic_copy.xml
        ├── ic_bookmark.xml
        ├── ic_settings.xml
        └── btn_style_bg.xml
```

---

## License

MIT
