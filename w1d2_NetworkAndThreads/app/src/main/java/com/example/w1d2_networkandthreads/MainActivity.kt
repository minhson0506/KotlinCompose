package com.example.w1d2_networkandthreads

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.w1d2_networkandthreads.ui.theme.W1d2_NetworkAndThreadsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W1d2_NetworkAndThreadsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(this.applicationContext)
                }
            }
        }
    }
}

@Composable
fun Greeting(context: Context) {
    val TAG = "w1d2_NetworkAndThreads"
    val string = remember { mutableStateOf("") }
    val mHandler: Handler = object :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            if (inputMessage.what == 0) {
                Log.i(TAG, "Message received ${inputMessage.obj}")
                string.value = inputMessage.obj.toString()
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
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

    if (isNetworkAvailable()) {
        Log.d(TAG, "Network is available")
        val myRunnable = Connection(mHandler)
        val myThread = Thread(myRunnable)
        myThread.start()
        DisplayText(text = string.value)
    } else {
        Log.d(TAG, "Network is not available")
    }
}

@Composable
fun DisplayText(text: String) {
    Text(text = text)
}
