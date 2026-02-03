package com.example.lab.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(model: CalculatorViewModel = viewModel()) {
    val buttons = listOf(
        listOf("C", "⌫", "%", "/"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "+/-", "=")
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = model.displayState,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, end = 12.dp),
            fontSize = 70.sp,
            color = Color.White,
            textAlign = TextAlign.End,
            maxLines = 1
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { char ->
                    val isOperator = char in listOf("/", "×", "-", "+", "=")
                    val isAction = char in listOf("C", "⌫", "%", "+/-")

                    Button(
                        onClick = { model.onAction(char) },
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                isOperator -> ApplePurple
                                isAction -> AppleLightGray
                                else -> AppleDarkGray
                            },
                            contentColor = if (isAction) Color.Black else Color.White
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = char, fontSize = 28.sp, fontWeight = FontWeight.Medium)
                    }
                }
                if (row.size < 4) {
                    repeat(4 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }
}