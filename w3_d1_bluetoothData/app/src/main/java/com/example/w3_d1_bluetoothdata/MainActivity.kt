package com.example.w3_d1_bluetoothdata

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.w3_d1_bluetoothdata.ui.theme.W3_d1_bluetoothDataTheme

class MainActivity : ComponentActivity() {
    private val TAG = "w3_d1_bluetoothdata"
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var model: BLEViewModel = BLEViewModel()

    private fun hasPermissions(): Boolean {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Log.d(TAG, "No Bluetooth LE capability")
            return false
        } else
            if ((checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(
                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
            ) {
                Log.d(TAG, "No fine location access")
                requestPermissions(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    1
                ); return true // assuming that the user grants permission
            }
        Log.i(TAG, "permissions ok")
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        setContent {
            W3_d1_bluetoothDataTheme() {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (hasPermissions()) bluetoothAdapter?.let { ShowDevices(it, model) }
                }
            }
        }
    }
}

@Composable
fun ShowDevices(bluetoothAdapter: BluetoothAdapter, model: BLEViewModel) {
    val TAG = "w3_d1_bluetoothdata"
    val scanner = bluetoothAdapter.bluetoothLeScanner
    val context = LocalContext.current
    val value: List<ScanResult>? by model.scanResults.observeAsState(null)
    val fScanning: Boolean by model.fScanning.observeAsState(false)
    var enabled by rememberSaveable { mutableStateOf(true) }
    var isConnect by remember { mutableStateOf(false) }
    val gattClientCallback = GattClientCallback(model = model)
    val heartRate by model.mBPM.observeAsState()
    Column(modifier = Modifier
        .padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            model.scanDevices(scanner, context)
        }) {
            Text("Scan devices")
        }
        Log.i(TAG, "device bluetooth: $value")
        Row() {
            Text(text = if (isConnect) "Connected:" else "Disconnect",
                modifier = Modifier
                    .height(20.dp))
            Text(text = if (heartRate != 0) "$heartRate bpm" else "",
                modifier = Modifier
                    .height(20.dp))
            Log.d(TAG, "ShowDevices: rate $heartRate")
        }

        Column(modifier = Modifier
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            value?.forEach { item ->
                Text(
                    text = if (item.device.name != null) item.device.name + " " + item.device.address + " " + item.rssi + "dbm"
                    else item.device.address + " " + item.rssi + "dbm",
                    color = if (!item.isConnectable) Color.Gray else Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable(enabled = enabled) {
                            Log.d(TAG, "ShowDevices: press item")
                            enabled = false

                            val bluetoothGatt =
                                item.device.connectGatt(context, false, gattClientCallback)
                            isConnect = bluetoothGatt.connect()
                        }
                )
            }
        }
    }

}