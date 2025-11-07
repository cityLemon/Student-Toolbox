package com.example.guyuefangyuan.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class CalculatorButtonType {
    NUMBER,
    OPERATION,
    FUNCTION,
    EQUALS,
    SCIENTIFIC
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    type: CalculatorButtonType = CalculatorButtonType.NUMBER,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )
    
    val backgroundColor = when (type) {
        CalculatorButtonType.NUMBER -> MaterialTheme.colorScheme.surface
        CalculatorButtonType.OPERATION -> MaterialTheme.colorScheme.tertiary
        CalculatorButtonType.FUNCTION -> MaterialTheme.colorScheme.secondary
        CalculatorButtonType.EQUALS -> MaterialTheme.colorScheme.primary
        CalculatorButtonType.SCIENTIFIC -> MaterialTheme.colorScheme.tertiaryContainer
    }
    
    val contentColor = when (type) {
        CalculatorButtonType.NUMBER -> MaterialTheme.colorScheme.onSurface
        CalculatorButtonType.OPERATION -> MaterialTheme.colorScheme.onTertiary
        CalculatorButtonType.FUNCTION -> MaterialTheme.colorScheme.onSecondary
        CalculatorButtonType.EQUALS -> MaterialTheme.colorScheme.onPrimary
        CalculatorButtonType.SCIENTIFIC -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.8f)
                    )
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
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
    modifier: Modifier = Modifier,
    hasError: Boolean = false,
    errorMessage: String = ""
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            if (hasError) {
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2
                )
            }
            Text(
                text = text,
                fontSize = if (text.length > 10) 36.sp else 48.sp,
                fontWeight = FontWeight.Light,
                color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}