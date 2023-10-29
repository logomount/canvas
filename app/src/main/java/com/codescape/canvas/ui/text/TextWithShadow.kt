package com.codescape.canvas.ui.text

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

/**
 * Created by Fedor Erofeev on 29.10.2023
 */

@Composable
fun TextWithShadow(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle.Default,
    shadowSize: Dp = 1.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")
    val offset by infiniteTransition.animateFloat(
        initialValue = LocalDensity.current.run { -2.dp.toPx() },
        targetValue = LocalDensity.current.run { 0.dp.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_rotation"
    )
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = remember(text) {
        textMeasurer.measure(
            text = text,
            style = textStyle
        )
    }
    val textSize = LocalDensity.current.run { textLayoutResult.size.toSize().toDpSize() }
    Canvas(
        modifier = modifier
            .size(textSize * 2)
    ) {
        translate(top = offset) {
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = textLayoutResult.size.width.toFloat() / 2,
                    y = textLayoutResult.size.height.toFloat() / 2
                ),
                color = Color.Black,
                shadow = Shadow(
                    color = Color.Gray,
                    offset = Offset(
                        x = 2.dp.toPx(),
                        y = offset * -2
                    ),
                    blurRadius = shadowSize.toPx()
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun TextWithShadowPreview() {
    TextWithShadow(
        text = "Text",
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp
        )
    )
}