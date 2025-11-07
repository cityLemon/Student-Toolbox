package com.example.guyuefangyuan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CalculatorButtonType {
    NUMBER,
    OPERATION,
    FUNCTION,
    EQUALS
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    type: CalculatorButtonType = CalculatorButtonType.NUMBER,
    onClick: () -> Unit
) {
    val backgroundColor = when (type) {
        CalculatorButtonType.NUMBER -> MaterialTheme.colorScheme.surface
        CalculatorButtonType.OPERATION -> MaterialTheme.colorScheme.tertiary
        CalculatorButtonType.FUNCTION -> MaterialTheme.colorScheme.secondary
        CalculatorButtonType.EQUALS -> MaterialTheme.colorScheme.primary
    }
    
    val contentColor = when (type) {
        CalculatorButtonType.NUMBER -> MaterialTheme.colorScheme.onSurface
        CalculatorButtonType.OPERATION -> MaterialTheme.colorScheme.onTertiary
        CalculatorButtonType.FUNCTION -> MaterialTheme.colorScheme.onSecondary
        CalculatorButtonType.EQUALS -> MaterialTheme.colorScheme.onPrimary
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .aspectRatio(1f)
            .padding(8.dp)
    ) {
        Text(
            text = symbol,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Composable
fun CalculatorDisplay(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
} 