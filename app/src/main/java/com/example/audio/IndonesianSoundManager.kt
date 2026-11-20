package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import com.example.model.Animal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Handles cheerful Indonesian Text-to-Speech voice prompts and synthesized
 * xylophone/marimba toy sound effects tailored for toddlers.
 */
class IndonesianSoundManager(context: Context) : TextToSpeech.OnInitListener {

    private val appContext = context.applicationContext
    private val audioScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var tts: TextToSpeech? = null
    @Volatile
    private var isTtsReady = false
    @Volatile
    var isSoundEnabled: Boolean = true

    private var pendingSpeech: String? = null

    init {
        tts = TextToSpeech(appContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val engine = tts ?: return
            val idLocale = Locale.Builder().setLanguage("id").setRegion("ID").build()
            val result = engine.setLanguage(idLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val legacyLocale = Locale.Builder().setLanguage("in").setRegion("ID").build()
                engine.setLanguage(legacyLocale)
            }
            // Cheerful, warm, clear toddler-friendly cadence
            engine.setPitch(1.16f)
            engine.setSpeechRate(0.88f)
            isTtsReady = true

            val queued = pendingSpeech
            pendingSpeech = null
            if (!queued.isNullOrBlank() && isSoundEnabled) {
                speakNow(queued)
            }
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
        if (!enabled) {
            stopSpeaking()
        }
    }

    fun stopSpeaking() {
        runCatching { tts?.stop() }
    }

    fun speak(text: String) {
        if (!isSoundEnabled) return
        if (!isTtsReady) {
            pendingSpeech = text
            return
        }
        speakNow(text)
    }

    private fun speakNow(text: String) {
        runCatching {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utt_${System.nanoTime()}")
        }
    }

    fun speakWelcome() {
        speak("Halo adik pintar! Ayo bermain tebak hewan yang seru!")
    }

    fun speakSoundQuestion(animal: Animal) {
        speak("Hewan manakah yang bersuara... ${animal.spokenSound} Ayo tebak!")
    }

    fun speakShadowQuestion(animal: Animal) {
        speak("Siapa ya di balik bayangan ini? ${animal.shadowClue}")
    }

    fun speakAnimalLearn(animal: Animal) {
        playPop()
        speak("Ini ${animal.name}! Suaranya ${animal.spokenSound} ${animal.funFact}")
    }

    fun speakCorrectCelebration(animal: Animal) {
        playSuccessFanfare()
        val cheers = listOf(
            "Horeee! Pintar sekali! Itu adalah ${animal.name}! ${animal.spokenSound}",
            "Yeay! Hebat sekali adik! Benar, itu ${animal.name}! ${animal.spokenSound}",
            "Wah, kamu jago sekali! Itu ${animal.name}! ${animal.spokenSound}"
        )
        speak(cheers.random())
    }

    fun speakGentleTryAgain(tappedAnimal: Animal) {
        playGentleBoing()
        speak("Hampir benar! Itu ${tappedAnimal.name}, suaranya ${tappedAnimal.spokenSound} Yuk coba lagi ya!")
    }

    fun speakVictory(stars: Int) {
        playVictoryJingle()
        speak("Horeee! Luar biasa! Kamu mendapat $stars bintang emas! Ayo main lagi!")
    }

    fun playPop() {
        if (!isSoundEnabled) return
        audioScope.launch {
            playMarimbaSequence(
                listOf(
                    ToneNote(587.33, 70, 0.35), // D5
                    ToneNote(880.00, 90, 0.40)  // A5
                )
            )
        }
    }

    fun playSuccessFanfare() {
        if (!isSoundEnabled) return
        audioScope.launch {
            playMarimbaSequence(
                listOf(
                    ToneNote(523.25, 110, 0.45), // C5
                    ToneNote(659.25, 110, 0.48), // E5
                    ToneNote(783.99, 120, 0.50), // G5
                    ToneNote(1046.50, 240, 0.55) // C6
                )
            )
        }
    }

    fun playGentleBoing() {
        if (!isSoundEnabled) return
        audioScope.launch {
            playMarimbaSequence(
                listOf(
                    ToneNote(392.00, 120, 0.35), // G4
                    ToneNote(329.63, 180, 0.32)  // E4
                )
            )
        }
    }

    fun playVictoryJingle() {
        if (!isSoundEnabled) return
        audioScope.launch {
            playMarimbaSequence(
                listOf(
                    ToneNote(523.25, 110, 0.45), // C5
                    ToneNote(659.25, 110, 0.45), // E5
                    ToneNote(783.99, 110, 0.48), // G5
                    ToneNote(1046.50, 160, 0.52), // C6
                    ToneNote(783.99, 100, 0.46), // G5
                    ToneNote(1046.50, 320, 0.56) // C6
                )
            )
        }
    }

    private data class ToneNote(
        val freqHz: Double,
        val durationMs: Int,
        val volume: Double
    )

    private fun playMarimbaSequence(notes: List<ToneNote>) {
        val sampleRate = 22050
        val totalSamples = notes.sumOf { (sampleRate * it.durationMs) / 1000 }
        if (totalSamples <= 0) return

        val pcm = ShortArray(totalSamples)
        var offset = 0

        for (note in notes) {
            val count = (sampleRate * note.durationMs) / 1000
            for (i in 0 until count) {
                val t = i.toDouble() / sampleRate
                // Warm marimba envelope: fast attack + soft exponential decay
                val attack = (i.toDouble() / (sampleRate * 0.008)).coerceAtMost(1.0)
                val decay = exp(-t * 9.5)
                val fundamental = sin(2.0 * PI * note.freqHz * t)
                val overtone = 0.25 * sin(2.0 * PI * note.freqHz * 2.0 * t) * exp(-t * 18.0)
                val sample = ((fundamental + overtone) * attack * decay * note.volume * Short.MAX_VALUE)
                    .toInt()
                    .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                if (offset + i < pcm.size) {
                    pcm[offset + i] = sample.toShort()
                }
            }
            offset += count
        }

        runCatching {
            val bufferBytes = pcm.size * 2
            val minBuf = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(bufferBytes)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBuf)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(pcm, 0, pcm.size)
            track.play()
            val totalDurationMs = notes.sumOf { it.durationMs }.toLong() + 120L
            Thread.sleep(totalDurationMs)
            track.stop()
            track.release()
        }
    }

    fun release() {
        audioScope.cancel()
        runCatching {
            tts?.stop()
            tts?.shutdown()
            tts = null
        }
    }
}
