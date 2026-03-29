package com.tekphreak.threadstyle

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tekphreak.threadstyle.databinding.ActivityZalgoBinding
import kotlin.random.Random

class ZalgoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityZalgoBinding
    private var sourceText: String = ""

    companion object {
        const val EXTRA_TEXT = "zalgo_source_text"

        // Combining characters above the baseline
        private val CHARS_UP = charArrayOf(
            '\u030d', '\u030e', '\u0304', '\u0305', '\u033f', '\u0311', '\u0306', '\u0310',
            '\u0352', '\u0357', '\u0351', '\u0307', '\u0308', '\u030a', '\u0342', '\u0343',
            '\u0344', '\u034a', '\u034b', '\u034c', '\u0303', '\u0302', '\u030c', '\u0350',
            '\u0300', '\u0301', '\u030b', '\u030f', '\u0312', '\u0313', '\u0314', '\u033d',
            '\u0309', '\u0363', '\u0364', '\u0365', '\u0366', '\u0367', '\u0368', '\u0369',
            '\u036a', '\u036b', '\u036c', '\u036d', '\u036e', '\u036f', '\u033e', '\u035b'
        )

        // Combining characters through the middle
        private val CHARS_MID = charArrayOf(
            '\u0315', '\u031b', '\u0340', '\u0341', '\u0358', '\u0321', '\u0322', '\u0327',
            '\u0328', '\u0334', '\u0335', '\u0336', '\u034f', '\u035c', '\u035d', '\u035e',
            '\u035f', '\u0360', '\u0362', '\u0338', '\u0337', '\u0361', '\u0489'
        )

        // Combining characters below the baseline
        private val CHARS_DOWN = charArrayOf(
            '\u0316', '\u0317', '\u0318', '\u0319', '\u031c', '\u031d', '\u031e', '\u031f',
            '\u0320', '\u0324', '\u0325', '\u0326', '\u0329', '\u032a', '\u032b', '\u032c',
            '\u032d', '\u032e', '\u032f', '\u0330', '\u0331', '\u0332', '\u0333', '\u0339',
            '\u033a', '\u033b', '\u033c', '\u0345', '\u0347', '\u0348', '\u0349', '\u034d',
            '\u034e', '\u0353', '\u0354', '\u0355', '\u0356', '\u0359', '\u035a'
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZalgoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sourceText = intent.getStringExtra(EXTRA_TEXT) ?: ""

        binding.crazinessSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateZalgo(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Trigger initial render at 50
        updateZalgo(50)

        binding.copyZalgoButton.setOnClickListener { copyZalgo() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun updateZalgo(craziness: Int) {
        val zalgoText = zalgoify(sourceText, craziness)
        binding.zalgoOutput.setText(zalgoText)

        val len = zalgoText.length
        binding.zalgoCharCounter.text = "$len / 500"
        binding.zalgoCharCounter.setTextColor(
            if (len > 500) 0xFFFF6B6B.toInt() else 0x80FFFFFF.toInt()
        )

        binding.crazinessLabel.text = "Craziness: ${crazinessLabel(craziness)}"
    }

    private fun crazinessLabel(craziness: Int) = when {
        craziness <= 20 -> "Mild"
        craziness <= 50 -> "Normal"
        craziness <= 75 -> "Cursed"
        craziness <= 90 -> "Demonic"
        else -> "He comes"
    }

    private fun zalgoify(text: String, craziness: Int): String {
        val rng = Random.Default
        // At craziness=1: max 1 per position. At craziness=100: max 15 per position.
        val maxPerPos = ((craziness / 100f) * 15).toInt().coerceAtLeast(1)
        return buildString {
            for (ch in text) {
                append(ch)
                // Leave spaces and newlines clean so the text stays readable
                if (ch == ' ' || ch == '\n') continue
                repeat(rng.nextInt(maxPerPos + 1)) { append(CHARS_UP[rng.nextInt(CHARS_UP.size)]) }
                repeat(rng.nextInt(maxPerPos + 1)) { append(CHARS_MID[rng.nextInt(CHARS_MID.size)]) }
                repeat(rng.nextInt(maxPerPos + 1)) { append(CHARS_DOWN[rng.nextInt(CHARS_DOWN.size)]) }
            }
        }
    }

    private fun copyZalgo() {
        val text = binding.zalgoOutput.text?.toString() ?: ""
        if (text.isEmpty()) {
            Snackbar.make(binding.root, "Nothing to copy!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("ThreadStyle Zalgo", text))
        Toast.makeText(this, "Zalgo copied", Toast.LENGTH_SHORT).show()
    }
}
