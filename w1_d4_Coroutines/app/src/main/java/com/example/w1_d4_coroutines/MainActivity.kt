package com.example.w1_d4_coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.w1_d4_coroutines.ui.theme.W1_d4_CoroutinesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MainActivity : ComponentActivity() {
    class Account {
        private var amount: Double = 0.0
        suspend fun deposit_coroutine(amount: Double) {
            val x = this.amount
            delay(1) // simulates processing time
         this.amount = x + amount
        }

        fun saldo(): Double = amount
    }

    /* Approximate measurement of the given block's execution time */
    fun withTimeMeasurement(title: String, isActive: Boolean = true, code: () -> Unit) {
        if (!isActive) return
        val timeStart = System.currentTimeMillis()
        code()
        val timeEnd = System.currentTimeMillis()
        println("operation in '$title' took ${(timeEnd - timeStart)} ms")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val mutex = Mutex()
        val tili2 = Account();
        super.onCreate(savedInstanceState)
        setContent {
            W1_d4_CoroutinesTheme() {
                Column() {
                    withTimeMeasurement("Single coroutine deposit 1000 times") {
                        runBlocking {
                            launch {
                                for (i in 1..1000)
                                    tili2.deposit_coroutine(0.0)
                            }
                        }
                    }
                    Greeting(name = "Saldo2 after the first coroutine: ${tili2.saldo()}")

                    withTimeMeasurement("Two coroutines together", isActive = true) {
                        runBlocking {
                            launch {
                                mutex.withLock {
                                    for (i in 1..1000) tili2.deposit_coroutine(1.0)
                                }
                            }
                            launch {
                                mutex.withLock {
                                    for (i in 1..1000) tili2.deposit_coroutine(1.0)
                                }
                            }
                        }
                    }
                    Greeting(name = "Saldo2 after the second coroutine: ${tili2.saldo()}")
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W1_d4_CoroutinesTheme {
        Greeting("Android")
    }
}