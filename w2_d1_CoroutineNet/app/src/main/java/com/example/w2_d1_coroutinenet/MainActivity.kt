package com.example.w2_d1_coroutinenet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.lifecycleScope
import com.example.w2_d1_coroutinenet.ui.theme.W2_d1_CoroutineNetTheme
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = URL("https://users.metropolia.fi/~sond/CSS_layout_part1/img/background.png")
        val context = this.applicationContext

        lifecycleScope.launch(Dispatchers.Main) {
            val image = getImg(context = context, url = url)
            setContent {
                W2_d1_CoroutineNetTheme {
                    Surface() {
                        if (image != null)
                            Image(bitmap = image.asImageBitmap(),
                                contentDescription = "content")
                    }
                }
            }

        }
    }
}

private suspend fun getImg(context: Context, url: URL): Bitmap? = withContext(Dispatchers.IO) {
    val TAG = "w2_d1_CoroutineNet"
    if (isNetworkAvailable(context = context)) {
        try {
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            return@withContext BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else Log.d(TAG, "Network is not available")
    return@withContext null
}


@SuppressLint("ServiceCast")
fun isNetworkAvailable(context: Context): Boolean {
    val connectionManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        networkCapabilities?.let { networkCapability ->
            return networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    } else {
        return networkCapabilities != null && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    return false
}

