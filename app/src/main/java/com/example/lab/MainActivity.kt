package com.example.lab

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.lab.ui.theme.MainScreen
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        setContent {
            ApplyCloudTheme {
                MainScreen()
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {}.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun ApplyCloudTheme(content: @Composable () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var themeColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    val window = (context as? Activity)?.window

    LaunchedEffect(Unit) {
        db.collection("settings").document("theme").addSnapshotListener { snapshot, _ ->
            val colorHex = snapshot?.getString("backgroundColor") ?: "#000000"
            val parsedColor = Color(android.graphics.Color.parseColor(colorHex))
            themeColor = parsedColor
            window?.statusBarColor = parsedColor.toArgb()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = themeColor) {
        content()
    }
}