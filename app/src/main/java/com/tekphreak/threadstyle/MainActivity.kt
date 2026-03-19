package com.tekphreak.threadstyle

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tekphreak.threadstyle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val styleButtons = listOf(
        "Normal" to "normal",
        "italic" to "italic",
        "bold italic" to "boldItalic",
        "bold cursive" to "boldCursive",
        "doublestruck" to "doublestruck",
        "redacted" to "redacted",
        "small caps" to "smallcaps",
        "asian" to "asian",
        "thai" to "thai",
        "underline" to "underline",
        "strike" to "strikethrough",
        "sup" to "superscript",
        "mono" to "mono",
        "witchy" to "fraktur",
        "bold witchy" to "boldFraktur",
        "script" to "script",
        "bold script" to "boldScript",
        "sans" to "sans",
        "sans bold" to "sansBold",
        "sans italic" to "sansItalic",
        "s b ital" to "sansBoldItalic",
        "circled" to "circled",
        "\uD83D\uDC4F clap" to "clap",
        "sArCaSm" to "sarcasm",
        "wizard" to "wizard",
        "ｗｉｄｅ" to "wide",
        "flip" to "flip",
        "\uD83C\uDD51ubble" to "bubble"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStyleButtons()
        setupCopyButton()
        setupLoadButton()
        setupCharCounter()
        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupStyleButtons() {
        val grid = binding.styleButtonGrid
        val dp4 = (4 * resources.displayMetrics.density).toInt()
        styleButtons.forEach { (label, style) ->
            val btn = Button(this).apply {
                text = label
                textSize = 11f
                isAllCaps = false
                setBackgroundResource(R.drawable.btn_style_bg)
                setTextColor(0xFFFFFFFF.toInt())
                setPadding(dp4, dp4 * 2, dp4, dp4 * 2)
                setOnClickListener { transformText(style) }
            }
            val params = android.widget.GridLayout.LayoutParams().apply {
                width = 0
                height = android.widget.GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1, 1f)
                setMargins(dp4, dp4, dp4, dp4)
            }
            grid.addView(btn, params)
        }
    }

    private fun setupCopyButton() {
        binding.copyButton.setOnClickListener { copyToClipboard() }
    }

    private fun setupLoadButton() {
        binding.loadButton.setOnClickListener { loadFromClipboard() }
    }

    private fun loadFromClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = clipboard.primaryClip?.getItemAt(0)?.coerceToText(this)?.toString() ?: ""
        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Clipboard is empty", Snackbar.LENGTH_SHORT).show()
            return
        }
        binding.composerInput.setText(text)
        binding.composerInput.setSelection(text.length)
        Snackbar.make(binding.root, "Loaded from clipboard", Snackbar.LENGTH_SHORT).show()
    }

    private fun setupCharCounter() {
        val editText = binding.composerInput
        editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val len = s?.length ?: 0
                binding.charCounter.text = "$len / 500"
            }
        })
    }

    private fun transformText(style: String) {
        val editText = binding.composerInput
        val start = editText.savedStart
        val end = editText.savedEnd

        if (start == end) {
            Snackbar.make(binding.root, "Select some text first", Snackbar.LENGTH_SHORT).show()
            return
        }

        val fullText = editText.text?.toString() ?: return
        val selectedText = fullText.substring(start, end)

        // Clean: reverse-map styled chars back to ASCII, strip combining chars
        val cleanText = buildString {
            var i = 0
            while (i < selectedText.length) {
                val c = selectedText[i]
                // Skip combining underline and strikethrough characters
                if (c == '\u0332' || c == '\u0336') {
                    i++
                    continue
                }
                // Handle surrogate pairs (supplementary Unicode chars)
                val charStr = if (c.isHighSurrogate() && i + 1 < selectedText.length && selectedText[i + 1].isLowSurrogate()) {
                    val pair = selectedText.substring(i, i + 2)
                    i++
                    pair
                } else {
                    c.toString()
                }
                // Look up in reverseMap, or keep as-is
                val reversed = CharMaps.reverseMap[charStr]
                append(reversed ?: charStr)
                i++
            }
        }

        val result = when (style) {
            "normal" -> cleanText
            "underline" -> cleanText.map { "$it\u0332" }.joinToString("")
            "strikethrough" -> cleanText.map { "$it\u0336" }.joinToString("")
            "clap" -> cleanText.split(" ").joinToString(" \uD83D\uDC4F ")
            "sarcasm" -> {
                var idx = 0
                cleanText.map { c ->
                    if (c.isLetter()) {
                        (if (idx % 2 == 0) c.lowercaseChar() else c.uppercaseChar()).also { idx++ }
                    } else {
                        c
                    }
                }.joinToString("")
            }
            else -> {
                val map = CharMaps.maps[style]
                if (map == null) {
                    cleanText
                } else {
                    buildString {
                        cleanText.forEach { c ->
                            append(map[c.toString()] ?: c.toString())
                        }
                    }
                }
            }
        }

        val before = fullText.substring(0, start)
        val after = fullText.substring(end)
        editText.setText(before + result + after)
        editText.setSelection(start, start + result.length)
    }

    private fun copyToClipboard() {
        val text = binding.composerInput.text?.toString() ?: ""
        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Nothing to copy!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("ThreadStyle", text))
        appendToLog(text)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        val prefs = getSharedPreferences("threadstyle_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("copy_tone", true)) playSuccessChord()
    }

    private fun playSuccessChord() {
        Thread {
            val sampleRate = 44100
            val noteDurationMs = 120
            val noteSamples = sampleRate * noteDurationMs / 1000
            val frequencies = listOf(523.25, 659.25, 783.99)      // C5, E5, G5

            val bufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize,
                AudioTrack.MODE_STREAM
            )
            track.play()

            frequencies.forEach { freq ->
                val buf = ShortArray(noteSamples) { i ->
                    val amplitude = if (i < noteSamples * 0.7) 1.0
                                    else 1.0 - (i - noteSamples * 0.7) / (noteSamples * 0.3)
                    (Short.MAX_VALUE * 0.6 * amplitude *
                        Math.sin(2 * Math.PI * freq * i / sampleRate)).toInt().toShort()
                }
                track.write(buf, 0, buf.size)
            }

            // Write silence to flush the last note through the hardware buffer
            // before stop() discards buffered audio
            track.write(ShortArray(bufSize), 0, bufSize)

            track.stop()
            track.release()
        }.start()
    }

    private fun appendToLog(text: String) {
        try {
            val logFile = java.io.File(filesDir, "log.txt")
            logFile.appendText("$text\n---\n")
        } catch (_: Exception) {}
    }
}
