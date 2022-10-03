package com.example.w3_d4_audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.w3_d4_audio.ui.theme.W3_d4_audioTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.time.LocalTime

class MainActivity : ComponentActivity() {

    val TAG = "w3_d4_audio"
    private var recRunning = false
    var recFile: File? = null

    private fun hasPermissions(): Boolean {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "No audio recorder access")
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return true // assuming that the user grants permission
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W3_d4_audioTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    if (hasPermissions()) Greeting()
                }
            }
        }
    }

    @Composable
    fun Greeting() {
        var time: Pair<String, String>? by remember { mutableStateOf(null) }
        val context = LocalContext.current
        Column() {
            Button(onClick = { GlobalScope.launch(Dispatchers.IO) { recordAudio(context = context) } }) {
                Text(text = "Start recording")
            }
            Button(onClick = { recRunning = false }) {
                Text(text = "Stop recording")
            }
            Button(onClick = { GlobalScope.launch(Dispatchers.IO) { time = playAudio() } }) {
                Text(text = "Play recorded")
            }
            if (time != null) {
                Text(text = "Time start to play: ${time!!.first}")
                Text(text = "Time stop to play: ${time!!.second}")
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun recordAudio(context: Context) {
        val recFileName = "testjv.raw"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        try {
            recFile = File(storageDir.toString() + "/" + recFileName)
            Log.d(TAG, "location of file: ${storageDir.toString()}")
        } catch (ex: IOException) {
            Log.e(TAG, "Can't create audio file $ex")
        }

        val minBufferSize = AudioRecord.getMinBufferSize(44100,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT)
        val aFormat =
            AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO).build()
        val recorder =
            AudioRecord.Builder().setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(aFormat).setBufferSizeInBytes(minBufferSize)
                .build()
        val audioData = ByteArray(minBufferSize)
        recorder.startRecording()

        try {
            recRunning = true
            val outputStream = FileOutputStream(recFile)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val dataOutputStream = DataOutputStream(bufferedOutputStream)
            Log.d(TAG, "recordAudio: start recording")
            while (recRunning) {
                val numofBytes = recorder.read(audioData, 0, minBufferSize)
                if (numofBytes > 0) {
                    dataOutputStream.write(audioData)
                }
            }
            recorder.stop()
            dataOutputStream.close()
        } catch (e: IOException) {
            Log.e(TAG, "Recording error $e")
        }
    }

    private fun playAudio(): Pair<String, String> {
        val inputStream: InputStream = FileInputStream(recFile)
        Log.d(TAG, "playAudio: file is ${recFile.toString()}")
        val minBufferSize = AudioTrack.getMinBufferSize(44100,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT)
        val builder = AudioTrack.Builder()
        val attribute: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
        val format: AudioFormat =
            AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO).build()
        val track = builder.setAudioAttributes(attribute).setAudioFormat(format)
            .setBufferSizeInBytes(minBufferSize).build()
        track!!.setVolume(0.2f)
        val startTime = LocalTime.now().toString()
        track!!.play()
        var i = 0
        val buffer = ByteArray(minBufferSize)
        try {
            i = inputStream.read(buffer, 0, minBufferSize)
            while (i != -1) {
                track!!.write(buffer, 0, i)
                i = inputStream.read(buffer, 0, minBufferSize)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Stream read error $e")
        }
        try {
            inputStream.close()
        } catch (e: IOException) {
            Log.e(TAG, "Close error $e")
        }
        track!!.stop()
        track!!.release()
        val stopTime = LocalTime.now().toString()
        return Pair(startTime, stopTime)

    }
}
