package com.tekphreak.threadstyle

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tekphreak.threadstyle.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var allEntries: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = getSharedPreferences("threadstyle_prefs", Context.MODE_PRIVATE)
        binding.checkCopyTone.isChecked = prefs.getBoolean("copy_tone", true)
        binding.checkCopyTone.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("copy_tone", isChecked).apply()
        }

        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                renderEntries(s?.toString() ?: "")
            }
        })

        binding.clearSearchButton.setOnClickListener {
            binding.searchField.setText("")
        }

        loadLog()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun deleteLogEntry(entry: String) {
        val logFile = java.io.File(filesDir, "log.txt")
        if (!logFile.exists()) return
        val remaining = logFile.readText()
            .split("---\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() && it != entry }
        logFile.writeText(
            if (remaining.isEmpty()) "" else remaining.joinToString("\n---\n") { it } + "\n---\n"
        )
    }

    private fun loadLog() {
        val logFile = java.io.File(filesDir, "log.txt")
        allEntries = if (logFile.exists()) {
            logFile.readText()
                .split("---\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .reversed()
        } else {
            emptyList()
        }
        renderEntries(binding.searchField.text?.toString() ?: "")
    }

    private fun renderEntries(filter: String) {
        val container = binding.logContainer
        container.removeAllViews()

        val filtered = if (filter.isBlank()) allEntries
                       else allEntries.filter { it.contains(filter, ignoreCase = true) }

        if (filtered.isEmpty()) return

        val dp4 = (4 * resources.displayMetrics.density).toInt()
        val dp8 = (8 * resources.displayMetrics.density).toInt()
        val dp12 = (12 * resources.displayMetrics.density).toInt()

        filtered.forEach { entry ->
            val tv = TextView(this).apply {
                text = entry
                textSize = 14f
                setTextColor(0xFFFFFFFF.toInt())
                setBackgroundResource(R.drawable.btn_style_bg)
                setPadding(dp12, dp8, dp12, dp8)
                setOnClickListener {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("ThreadStyle", entry))
                    Toast.makeText(this@SettingsActivity, "Copied", Toast.LENGTH_SHORT).show()
                }
                setOnLongClickListener {
                    deleteLogEntry(entry)
                    allEntries = allEntries.filter { it != entry }
                    renderEntries(binding.searchField.text?.toString() ?: "")
                    true
                }
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(dp4, dp4, dp4, dp4) }
            container.addView(tv, params)
        }
    }
}
