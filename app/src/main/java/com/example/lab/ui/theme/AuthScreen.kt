package com.example.lab.ui.theme

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val securityManager = remember { SecurityManager(context) }

    var passKeyInput by remember { mutableStateOf("") }
    var isSetupMode by remember { mutableStateOf(!securityManager.isPassKeySet()) }
    var errorMessage by remember { mutableStateOf("") }

    // Биометрия
    fun authenticateBiometric() {
        if (activity == null || isSetupMode) return

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onAuthSuccess()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Биометрия")
            .setSubtitle("Используйте отпечаток для входа")
            .setNegativeButtonText("Использовать PIN приложения")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    LaunchedEffect(isSetupMode) {
        if (!isSetupMode) {
            delay(300)
            authenticateBiometric()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSetupMode) "Установите Pass Key" else "Введите Pass Key",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = passKeyInput,
            onValueChange = { if (it.length <= 4) passKeyInput = it },
            label = { Text("PIN-код") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isSetupMode) {
                    if (passKeyInput.length >= 4) {
                        securityManager.savePassKey(passKeyInput)
                        isSetupMode = false
                        passKeyInput = ""
                    } else {
                        errorMessage = "Минимум 4 символа"
                    }
                } else {
                    if (securityManager.validatePassKey(passKeyInput)) {
                        onAuthSuccess()
                    } else {
                        errorMessage = "Неверный пароль"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSetupMode) "Сохранить" else "Войти")
        }

        if (!isSetupMode) {
            TextButton(onClick = { authenticateBiometric() }) {
                Text("Использовать биометрию", color = Color.LightGray)
            }
        }
    }
}