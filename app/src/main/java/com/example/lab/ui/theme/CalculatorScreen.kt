package com.example.lab.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(model: CalculatorViewModel = viewModel()) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Данные кнопок
    val buttons = listOf(
        listOf("C", "⌫", "%", "/"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "+/-", "=")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CalculatorDisplay(
                        text = model.displayState,
                        textSize = 48.sp
                    )
                }

                Box(modifier = Modifier.weight(0.6f)) {
                    ButtonsGrid(
                        buttons = buttons,
                        onAction = { model.onAction(it) },
                        buttonAspectRatio = 1.8f
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                CalculatorDisplay(
                    text = model.displayState,
                    textSize = 70.sp,
                    modifier = Modifier.weight(1f)
                )

                ButtonsGrid(
                    buttons = buttons,
                    onAction = { model.onAction(it) },
                    buttonAspectRatio = 1f
                )
            }
        }
    }
}

@Composable
fun CalculatorDisplay(
    text: String,
    textSize: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(bottom = 16.dp, end = 12.dp),
            fontSize = textSize,
            color = Color.White,
            textAlign = TextAlign.End,
            maxLines = 1,
            lineHeight = textSize
        )
    }
}

@Composable
fun ButtonsGrid(
    buttons: List<List<String>>,
    onAction: (String) -> Unit,
    buttonAspectRatio: Float
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { char ->
                    val isOperator = char in listOf("/", "×", "-", "+", "=")
                    val isAction = char in listOf("C", "⌫", "%", "+/-")

                    Button(
                        onClick = { onAction(char) },
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(buttonAspectRatio),
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
                        Text(
                            text = char,
                            fontSize = if (buttonAspectRatio > 1.5f) 20.sp else 28.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}