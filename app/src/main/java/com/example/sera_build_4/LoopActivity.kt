package com.example.sera_build_4
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sera_build_4i.Player
import java.util.*


class LoopActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    var player = Player()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rep)

        Log.d("Activity:", "Opened")

        player.playAudio_Loop()

        // Check and request RECORD_AUDIO permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        } else {
            startSpeechRecognition()
        }
    }

    fun startSpeechRecognition() {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(i: Int) {}

            override fun onResults(results: Bundle) {
                // Called when the recognition is successful and final results are available
                val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (data != null && data.isNotEmpty()) {
                    val recognizedText = data[0]
                    handleSpeechInput(recognizedText)
                }
            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle?) {}

        })


        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer.startListening(speechIntent)
    }

    private fun handleSpeechInput(recognizedText: String) {
        // Compare recognizedText with desired command or keyword
        val commandA= "yes"
        val commandB = "no"

        if (recognizedText.equals(commandA, ignoreCase = true)) {
            onProceed()
        } else if (recognizedText.equals(commandB, ignoreCase = true)) {
            player.playAudio_Closing()
            finish()
        } else {
            player.playAudio_notRecognized()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 1
    }

    private fun onProceed() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        Log.d("Activity:", "Closed")
    }
}


