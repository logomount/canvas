package com.codescape.canvas.ui.shaders

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Created by Fedor Erofeev on 23.10.2023
 */

@Composable
fun ShaderBrushGradient(
    strokeWidth: Dp = 16.dp,
    cornerRadius: Dp = 16.dp,
    text: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")
    val gradientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_rotation"
    )
    val brush = remember(gradientRotation) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val angleRadian = Math.toRadians(gradientRotation.toDouble())
                val endX = size.center.x + (size.maxDimension / 2) * cos(angleRadian).toFloat()
                val endY = size.center.y + (size.maxDimension / 2) * sin(angleRadian).toFloat()
                return LinearGradientShader(
                    colors = listOf(Color.Green, Color.Blue),
                    from = size.center,
                    to = Offset(
                        x = endX,
                        y = endY
                    ),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .drawBehind {
                inset(
                    top = strokeWidth.toPx() / 2,
                    right = strokeWidth.toPx() / 2,
                    bottom = strokeWidth.toPx() / 2,
                    left = strokeWidth.toPx() / 2
                ) {
                    drawRoundRect(
                        cornerRadius = CornerRadius(
                            x = cornerRadius.toPx(),
                            y = cornerRadius.toPx()
                        ),
                        brush = brush,
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
            .padding(strokeWidth * 2)
    ) {
        Text(
            text = text,
            fontSize = 50.sp
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun ShaderBrushGradientPreview() {
    ShaderBrushGradient(text = "Click Me")
}