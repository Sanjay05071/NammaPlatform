package com.nammaplatform

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class PlatformDetailActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    companion object {
        const val EXTRA_TRAIN_INDEX = "TRAIN_INDEX"
        const val EXTRA_STATION_ID = "STATION_ID"
    }

    private lateinit var tts: TextToSpeech
    private var ttsReady = false
    private lateinit var train: Train
    private lateinit var btnSpeak: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_platform_detail)

        // Load data
        val stationId = intent.getStringExtra(EXTRA_STATION_ID) ?: ""
        val trainIndex = intent.getIntExtra(EXTRA_TRAIN_INDEX, 0)
        val stations = DataLoader.loadStations(this)
        val station = stations.find { it.id == stationId } ?: run { finish(); return }
        train = station.trains[trainIndex]

        // Initialize TTS
        tts = TextToSpeech(this, this)

        // Bind views
        bindHeader(train)
        bindPlatformCard(train)
        bindCoachLayout(train)
        bindStandingInfo(train)

        // Speak button
        btnSpeak = findViewById(R.id.btnSpeak)
        btnSpeak.setOnClickListener {
            speakAnnouncement()
        }

        // Back
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun bindHeader(train: Train) {
        findViewById<TextView>(R.id.tvTrainNameDetail).text = train.nameKannada
        findViewById<TextView>(R.id.tvTrainNumberDetail).text = "${train.number} → ${train.destinationKannada}"
    }

    private fun bindPlatformCard(train: Train) {
        findViewById<TextView>(R.id.tvPlatformBig).text = train.platform.toString()
        findViewById<TextView>(R.id.tvArrivalDetail).text = "ಬರುವ ಸಮಯ: ${train.arrivalTime}"
        val statusView = findViewById<TextView>(R.id.tvStatusDetail)
        statusView.text = train.statusKannada
        if (train.status == "delayed") {
            statusView.setTextColor(getColor(R.color.delayed_red))
        } else {
            statusView.setTextColor(getColor(R.color.railway_yellow))
        }
    }

    private fun bindCoachLayout(train: Train) {
        val container = findViewById<LinearLayout>(R.id.llFullCoachLayout)
        CoachViewHelper.populateCoachLayout(
            context = this,
            container = container,
            coaches = train.coaches,
            compact = false
        )
    }

    private fun bindStandingInfo(train: Train) {
        val generalIndex = train.coaches.indexOfFirst { it.type == "GENERAL" }
        val ladiesIndex = train.coaches.indexOfFirst { it.type == "LADIES" }

        val info = buildString {
            if (generalIndex >= 0) {
                appendLine("🟢 ಸಾಮಾನ್ಯ ಡಬ್ಬಿ: ಡಬ್ಬಿ ಸಂಖ್ಯೆ ${generalIndex + 1} (ಇಂಜಿನ್ ಕಡೆಯಿಂದ)")
                appendLine("   General Coach: Coach #${generalIndex + 1} from engine")
            }
            if (ladiesIndex >= 0) {
                appendLine("🩷 ಮಹಿಳಾ ಡಬ್ಬಿ: ಡಬ್ಬಿ ಸಂಖ್ಯೆ ${ladiesIndex + 1}")
                append("   Ladies Coach: Coach #${ladiesIndex + 1} from engine")
            }
        }

        findViewById<TextView>(R.id.tvStandingInfo).text = info
    }

    private fun speakAnnouncement() {
        if (!ttsReady) {
            Toast.makeText(this, "TTS ಲೋಡ್ ಆಗುತ್ತಿದೆ...", Toast.LENGTH_SHORT).show()
            return
        }

        btnSpeak.isEnabled = false
        btnSpeak.text = "ಮಾತನಾಡುತ್ತಿದ್ದಾರೆ..."

        // Try Kannada locale; fall back to default if unavailable
        val kannadaLocale = Locale("kn", "IN")
        val result = tts.setLanguage(kannadaLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback: use default locale and speak the announcement anyway
            tts.setLanguage(Locale.getDefault())
        }

        tts.setSpeechRate(0.85f)  // Slightly slower for clarity
        tts.setPitch(1.0f)

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "announcement")

        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                runOnUiThread {
                    btnSpeak.isEnabled = true
                    btnSpeak.text = "ಘೋಷಣೆ ಕೇಳಿ (Speak Announcement)"
                }
            }
            override fun onError(utteranceId: String?) {
                runOnUiThread {
                    btnSpeak.isEnabled = true
                    btnSpeak.text = "ಘೋಷಣೆ ಕೇಳಿ (Speak Announcement)"
                    Toast.makeText(
                        this@PlatformDetailActivity,
                        "TTS ದೋಷ. ದಯವಿಟ್ಟು ಸೆಟ್ಟಿಂಗ್ ಪರಿಶೀಲಿಸಿ.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        tts.speak(train.announcementKannada, TextToSpeech.QUEUE_FLUSH, params, "announcement")
    }

    override fun onInit(status: Int) {
        ttsReady = (status == TextToSpeech.SUCCESS)
        if (!ttsReady) {
            Toast.makeText(this, "TTS ಪ್ರಾರಂಭಿಸಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
