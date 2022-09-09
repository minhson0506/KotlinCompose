package com.example.w2_d3_locationmap

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.w2_d3_locationmap.ui.theme.W2_d3_LocationMapTheme
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*
import kotlin.math.round


class MainActivity : ComponentActivity() {
    private val TAG = "w2d3LocationMap"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission(this, this)
        val context = this.applicationContext
        val activity = this@MainActivity

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContent {
            W2_d3_LocationMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column() {
                        getLocation(context = context, activity = activity)
                    }
                }
            }
        }
    }
}

@Composable
fun composeMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply { id = R.id.map }
    }
    return mapView
}

@Composable
fun showMap(geoPoint: GeoPoint, address: String) {
    val map = composeMap()
    var mapInitialized by remember(map) { mutableStateOf(false) }
    if (!mapInitialized) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(17.0)
        map.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }

    val marker = Marker(map)

    AndroidView(factory = { map }) {
        address ?: return@AndroidView
        it.controller.setCenter(geoPoint)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.position = geoPoint
        marker.showInfoWindow()
        marker.title = address + "; lat ${geoPoint.latitude} & long ${geoPoint.longitude}"
        map.overlays.clear()
        map.overlays.add(marker)
        map.invalidate()
    }
}

fun getAddress(context: Context, lat: Double, long: Double): String {
    var TAG = "w2_d3_LocationMap"
    val geocoder = Geocoder(context, Locale.getDefault())
    var address = ""
    try {
        address = if (Build.VERSION.SDK_INT >= 33) {
            val location = geocoder.getFromLocation(lat, long, 1)
            location.first().getAddressLine(0)
        } else {
            geocoder.getFromLocation(lat, long, 1)?.first()?.getAddressLine(0) ?: ""
        }
    } catch (e: Error) {
        Log.d(TAG, "getAddress: not response")
    }
    return address
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
    var maxSpeed by remember {
        mutableStateOf(0.0)
    }
    var preLocation: Location? = null
    var long by remember {
        mutableStateOf(0.0)
    }
    var lat by remember {
        mutableStateOf(0.0)
    }

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
                if (preLocation != null) {
                    distance = round(location.distanceTo(preLocation)).toDouble()
                } else {
                    preLocation = location
                }
                speed = round(location.speed).toDouble()
                if (speed > maxSpeed) maxSpeed = speed
                Log.d(TAG, "distance is: $distance")
                Log.d(TAG, "speed is: $speed")
                Log.d(TAG,
                    "location latitude: ${location.latitude} and longitude ${location.longitude}")
                //preLocation = location
                long = location.longitude
                lat = location.latitude
            }
        }
    }

    Column() {
        Text(text = "Distance is: $distance m")
        Text(text = "Speed is: $speed m/s")
        Text(text = "Fastest speed is: $maxSpeed m/s")
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
        if (lat != 0.0 && long != 0.0) showMap(geoPoint = GeoPoint(lat, long),
            address = getAddress(context = context, lat, long))
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