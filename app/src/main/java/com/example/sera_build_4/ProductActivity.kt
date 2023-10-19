package com.example.sera_build_4
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import com.example.sera_build_4i.Player
import com.example.sera_build_4.R
import java.util.*

class ProductActivity : AppCompatActivity() {

    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val label = intent.getStringExtra("LABEL") ?: "No Label"

        // Find the TextView in your layout
        val resultTextView: TextView = findViewById(R.id.textView4)

        // Set the label to the TextView
        resultTextView.text = label

        tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {

                tts.language = Locale.US
                tts.setSpeechRate(0.9f)
                tts.speak(label, TextToSpeech.QUEUE_ADD, null)
            }

        })
    }

}
