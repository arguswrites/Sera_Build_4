package com.example.sera_build_4i
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*


@Suppress("DEPRECATION")
class Player : AppCompatActivity(){

    private lateinit var mediaPlayer: MediaPlayer

    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun playAudio_Greeting() {

        try {

            tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it== TextToSpeech.SUCCESS){

                    tts.language = Locale.US
                    tts.setSpeechRate(0.9f)
                    tts.speak("Good day. Sera is here to you", TextToSpeech.QUEUE_ADD, null)

                }
            })

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun playAudio_Start() {

        try {

            tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it== TextToSpeech.SUCCESS){

                    tts.language = Locale.US
                    tts.setSpeechRate(0.9f)
                    tts.speak("Let's start when you're ready, yes?", TextToSpeech.QUEUE_ADD, null)
                }
            })

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun playAudio_notRecognized() {

        try {

            tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it== TextToSpeech.SUCCESS){

                    tts.language = Locale.US
                    tts.setSpeechRate(0.9f)
                    tts.speak("Command not recognized. Please try again.", TextToSpeech.QUEUE_ADD, null)
                }
            })

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun playAudio_Loop() {

        try {
            tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it== TextToSpeech.SUCCESS){

                    tts.language = Locale.US
                    tts.setSpeechRate(0.9f)
                    tts.speak("Would you like to scan another product?", TextToSpeech.QUEUE_ADD, null)
                }
            })

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun playAudio_Scan() {

        try {
            tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if(it== TextToSpeech.SUCCESS){

                    tts.language = Locale.US
                    tts.setSpeechRate(0.9f)
                    tts.speak("Scanning for Product. Please Wait", TextToSpeech.QUEUE_ADD, null)
                }
            })

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun playAudio_Product(){
        tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it== TextToSpeech.SUCCESS){

                tts.language = Locale.US
                tts.setSpeechRate(0.9f)
                tts.speak("Shall we proceed?", TextToSpeech.QUEUE_ADD, null)
            }
        })

    }

    fun playAudio_Closing(){
        tts  = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it== TextToSpeech.SUCCESS){

                tts.language = Locale.US
                tts.setSpeechRate(0.9f)
                tts.speak("I am now closing. Till next time.", TextToSpeech.QUEUE_ADD, null)
            }
        })

    }

}