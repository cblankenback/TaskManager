package com.cst3115.enterprise.taskmanager.ui.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume

class UserLocationViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    // We'll store latitude and longitude
    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> = _latitude

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> = _longitude

    // Call this method once you know permissions are granted
    fun fetchLocation() {
        viewModelScope.launch {
            Log.d("UserLocationViewModel", "Attempting to fetch location...")
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val location = fusedLocationClient.getCurrentLocation(
                    com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).awaitOrNull()

                if (location != null) {
                    Log.d("UserLocationViewModel", "Location fetched: Latitude=${location.latitude}, Longitude=${location.longitude}")
                    _latitude.value = location.latitude
                    _longitude.value = location.longitude
                } else {
                    Log.e("UserLocationViewModel", "Failed to fetch location: Location is null")
                    // Optionally, you can try to request a new location or handle this case
                }
            } else {
                Log.e("UserLocationViewModel", "Cannot fetch location: Permissions not granted")
                // Handle permission not granted
            }
        }
    }
}

// A small suspend extension to await the location result:
suspend fun <T> Task<T>.awaitOrNull(): T? = suspendCancellableCoroutine { cont ->
    addOnCompleteListener {
        if (it.isSuccessful) {
            cont.resume(it.result)
        } else {
            cont.resume(null)
        }
    }
}
