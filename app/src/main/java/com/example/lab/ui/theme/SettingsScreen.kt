package com.example.lab.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val securityManager = remember { SecurityManager(context) }

    var currentPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки безопасности", fontSize = 22.sp, color = ApplePurple, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        if (!isVerified) {
            Text("Введите текущий PIN", color = Color.Gray)
            OutlinedTextField(
                value = currentPin,
                onValueChange = { if (it.length <= 4) currentPin = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (securityManager.validatePassKey(currentPin)) {
                        isVerified = true
                        message = ""
                    } else {
                        message = "Неверный текущий PIN"
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Подтвердить")
            }
        } else {
            Text("Введите новый PIN", color = Color.Gray)
            OutlinedTextField(
                value = newPin,
                onValueChange = { if (it.length <= 4) newPin = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (newPin.length >= 4) {
                        securityManager.savePassKey(newPin)
                        message = "Пароль успешно изменен!"
                        newPin = ""
                        currentPin = ""
                    } else {
                        message = "PIN должен быть от 4 цифр"
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Сохранить новый PIN")
            }
        }

        if (message.isNotEmpty()) {
            Text(message, color = if (message.contains("успешно")) Color.Green else Color.Red, modifier = Modifier.padding(top = 16.dp))
        }
    }
}