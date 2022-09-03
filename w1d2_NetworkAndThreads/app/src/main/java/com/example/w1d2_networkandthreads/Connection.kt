package com.example.w1d2_networkandthreads

import android.os.Handler
import android.util.Log
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Connection(mHandler: Handler) : Runnable {
    private val TAG = "w1d2_NetworkAndThreads"
    private val myHandler = mHandler
    override fun run() {
        try {
            val myUrl = URL("https://users.metropolia.fi/~jarkkov/koe.txt")
            val myConnection = myUrl.openConnection() as HttpURLConnection
            myConnection.requestMethod = "GET"
            myConnection.doOutput = true
            val ostream = myConnection.outputStream
            ostream.bufferedWriter().use { it.write("") }

            val isStream: InputStream = myConnection.inputStream
            val allText = isStream.bufferedReader().use { it.readText() }

            val result = StringBuilder()
            result.append(allText)
            val string = result.toString()

            val messaage = myHandler.obtainMessage()
            messaage.what = 0
            messaage.obj = string
            myHandler.sendMessage(messaage)
        } catch (e: Exception) {
            Log.d(TAG, "error when connection")
        }
    }
}