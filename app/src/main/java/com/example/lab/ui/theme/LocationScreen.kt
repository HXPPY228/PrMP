package com.example.lab.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.BatteryManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    // Храним объект локации, чтобы динамически подставлять в Intent
    var lastKnownLocation by remember { mutableStateOf<android.location.Location?>(null) }
    var coords by remember { mutableStateOf("Нажми ОБНОВИТЬ") }
    var address by remember { mutableStateOf("Адрес: Поиск...") }
    var batteryLevel by remember { mutableIntStateOf(0) }

    // Geocoder API
    fun updateAddress(lat: Double, lng: Double) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                address = "${addr.locality ?: "Неизвестно"}, ${addr.thoroughfare ?: "Центр"}"
            }
        } catch (e: Exception) {
            address = "Адрес не определен (нужен инет)"
        }
    }

    // Battery API
    LaunchedEffect(Unit) {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (locationPermissionState.status.isGranted) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("SYSTEM DASHBOARD", color = ApplePurple, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppleDarkGray)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ТЕКУЩАЯ ГЕОПОЗИЦИЯ", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(coords, color = Color.LightGray)
                        Text(address, color = Color.White, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppleDarkGray)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🔋 ЗАРЯД БАТАРЕИ: ", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("$batteryLevel%", color = if (batteryLevel > 20) Color.Green else Color.Red, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (locationPermissionState.status.isGranted) {
                            fetchLocation(fusedLocationClient) { loc ->
                                lastKnownLocation = loc
                                coords = "Широта: ${loc.latitude}\nДолгота: ${loc.longitude}"
                                updateAddress(loc.latitude, loc.longitude)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ApplePurple)
                ) {
                    Text("ПОЛУЧИТЬ КООРДИНАТЫ")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = lastKnownLocation != null,
                    onClick = {
                        lastKnownLocation?.let { loc ->
                            val uri = "geo:${loc.latitude},${loc.longitude}?q=${loc.latitude},${loc.longitude}(Моя+позиция)"
                            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppleDarkGray)
                ) {
                    Text("ОТКРЫТЬ В КАРТАХ", color = Color.White)
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Нужны права на GPS", color = Color.White)
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Дать разрешение")
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchLocation(
    client: com.google.android.gms.location.FusedLocationProviderClient,
    onSuccess: (android.location.Location) -> Unit
) {
    client.lastLocation.addOnSuccessListener { location ->
        if (location != null) onSuccess(location)
    }
}