package com.example.w2_d3_locationmap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.w2_d3_locationmap.ui.theme.W2_d3_LocationMapTheme
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import java.math.BigDecimal
import kotlin.math.round

class MainActivity : ComponentActivity() {
    private val TAG = "w2d3LocationMap"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission(this, this)
        setContent {
            W2_d3_LocationMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    getLocation(this, this)
                }
            }
        }
    }
}

@Composable
fun getLocation(context: Context, activity: Activity) {
    val TAG = "w2d3LocationMap"
    var distance by remember {
        mutableStateOf(0.0)
    }
    var speed by remember {
        mutableStateOf(0.0)
    }
    var preLocation: Location? = null


    var fusedLocationCLient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    if (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationCLient.lastLocation.addOnSuccessListener {
            preLocation = it
            Log.d(TAG,
                "last location latitude: ${it?.latitude} and longitude: ${it?.longitude}")
        }
    }

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                if (preLocation != null) distance = location.distanceTo(preLocation).toDouble()
                speed = round(location.speed).toDouble()
                Log.d(TAG, "distance is: $distance")
                Log.d(TAG, "speed is: $speed")
                Log.d(TAG,
                    "location latitude: ${location.latitude} and longitude ${location.longitude}")
                preLocation = location
            }
        }
    }

    Column() {
        Text(text = "Distance is: $distance")
        Text(text = "Speed is: $speed")
        Button(onClick = {
            val locationRequest = LocationRequest.create().setInterval(1000)
                .setPriority(PRIORITY_HIGH_ACCURACY)
            fusedLocationCLient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }) {
            Text(text = "Get Location")
        }
        Button(onClick = {
            fusedLocationCLient.removeLocationUpdates(locationCallback)
        }) {
            Text(text = "Stop getting Location")
        }
    }
}

fun checkPermission(context: Context, activity: Activity) {
    if ((Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    ) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            0)
    }
    if ((Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    ) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            0)
    }

    if ((Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
    ) {
        ActivityCompat.requestPermissions(activity,
            arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            0)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    W2_d3_LocationMapTheme {
        Greeting("Android")
    }
}