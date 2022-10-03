package com.example.w3_d2_graph

import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BLEViewModel : ViewModel() {
    private val SCAN_PERIOD: Long = 5000
    val scanResults = MutableLiveData<List<ScanResult>>(null)
    private val results = java.util.HashMap<String, ScanResult>()
    val mBPM = MutableLiveData(0)
    val graph = MutableLiveData(mutableListOf<Entry>())

    private val TAG = "w3_d2_graph"

    fun scanDevices(scanner: BluetoothLeScanner) {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setReportDelay(0)
                .build()

            scanner.startScan(null, settings, leScanCallback)
            delay(SCAN_PERIOD)
            scanner.stopScan(leScanCallback)
            scanResults.postValue(results.values.toList())
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceAddress = device.address
            results!![deviceAddress] = result
            Log.d(TAG, "Device address: $deviceAddress (${result.isConnectable})")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.i(TAG, "onScanFailed: failed")
        }
    }

}