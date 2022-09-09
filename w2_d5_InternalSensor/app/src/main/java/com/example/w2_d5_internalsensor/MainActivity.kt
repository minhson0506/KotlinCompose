package com.example.w2_d5_internalsensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.w2_d5_internalsensor.ui.theme.W2_d5_InternalSensorTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private val TAG = "w2_d5_InternalSensor"

    companion object {
        private lateinit var sm: SensorManager
        private var sTemperature: Sensor? = null
        private var sensorViewModel = SensorViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sm.getSensorList(Sensor.TYPE_ALL).forEach {
            Log.d(TAG, "sensor is ${it.name}")
        }

        sTemperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        setContent {
            W2_d5_InternalSensorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ShowSensorData(sensorViewModel = sensorViewModel)
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun onSensorChanged(p0: SensorEvent?) {
        p0 ?: return
        if (p0.sensor == sTemperature) {
            sensorViewModel.updateValue(p0.values[0].toString())
            Log.d(TAG, "onSensorChanged: ${p0.values[0]}")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged ${p0?.name}: $p1")
    }

    override fun onResume() {
        super.onResume()
        sTemperature?.also { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }
}

@Composable
fun ShowSensorData(sensorViewModel: SensorViewModel) {
    val value by sensorViewModel.value.observeAsState()
    if (value != null) Text("Temperature is $value", Modifier.padding(8.dp))
    else Text(text = "Temperature is not available", Modifier.padding(8.dp))

}
