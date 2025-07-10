package com.example.happyplaceapp

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.Flow

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    fun fetchLocation() {
        viewModelScope.launch {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    _location.value = loc
                }
            } catch (e: SecurityException) {
                // Kein Zugriff ohne Berechtigung
            }
        }
    }
}
