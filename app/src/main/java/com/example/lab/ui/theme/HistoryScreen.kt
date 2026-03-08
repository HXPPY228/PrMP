package com.example.lab.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HistoryScreen() {
    val db = FirebaseFirestore.getInstance()
    var historyList by remember { mutableStateOf(listOf<String>()) }
    var isDark by remember { mutableStateOf(true) }


    val textColor = if (isDark) Color.White else Color.Black

    LaunchedEffect(Unit) {
        db.collection("settings").document("theme").addSnapshotListener { snapshot, _ ->
            val color = snapshot?.getString("backgroundColor") ?: "#000000"
            isDark = color == "#000000"
        }

        // Грузим историю
        db.collection("history")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                historyList = snapshot?.map {
                    "${it.getString("expression")} = ${it.getString("result")}"
                } ?: emptyList()
            }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = {
                val newColor = if (isDark) "#FFFFFF" else "#000000"
                db.collection("settings").document("theme").update("backgroundColor", newColor)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color.White else Color.Black)
        ) {
            Text(if (isDark) "Светлая тема" else "Темная тема", color = if (isDark) Color.Black else Color.White)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(historyList) { item ->
                Text(text = item, color = textColor, modifier = Modifier.padding(vertical = 12.dp))
                HorizontalDivider(color = textColor.copy(alpha = 0.3f))
            }
        }
    }
}