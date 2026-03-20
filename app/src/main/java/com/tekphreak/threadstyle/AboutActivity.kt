package com.tekphreak.threadstyle

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tekphreak.threadstyle.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.versionText.text = "v${BuildConfig.VERSION_NAME}"

        binding.websiteButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://tekphreak.com")))
        }

        binding.donateButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/tekphreak")))
        }

        playChord()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun playChord() {
        Thread {
            val sampleRate = 44100
            val noteDurationMs = 120
            val noteSamples = sampleRate * noteDurationMs / 1000
            val frequencies = listOf(523.25, 659.25, 783.99) // C5, E5, G5

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

            track.write(ShortArray(bufSize), 0, bufSize)
            track.stop()
            track.release()
        }.start()
    }
}
