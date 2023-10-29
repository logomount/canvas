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
import androidx.compose.ui.text.font.FontWeight
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
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = 100.sp,
        fontWeight = FontWeight.W600
    ),
    shadowSize: Dp = 8.dp
) {

    // Step 1. Create infinite transition to play animation infinitely.
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Step 2. Animate the offset of the shadow from -16.dp to 0.dp indefinitely.
    val offset by infiniteTransition.animateFloat(
        initialValue = LocalDensity.current.run { -16.dp.toPx() },
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

    // Step 3. Create a TextMeasurer to measure text metrics for text layout.
    val textMeasurer = rememberTextMeasurer()

    // Step 4. Measure the text and produce a TextLayoutResult.
    val textLayoutResult = remember(text) {
        textMeasurer.measure(
            text = text,
            style = textStyle
        )
    }

    // Step 5. Determine the size of the Text based on its measured dimensions.
    val textSize = LocalDensity.current.run { textLayoutResult.size.toSize().toDpSize() }

    // Step 6. Create a Canvas to draw the Text.
    Canvas(
        modifier = modifier
            .size(textSize * 2)
    ) {

        // Step 7. Apply a translation transformation to the drawing operations within this block
        translate(top = offset) {

            // Step 8. Draw the Text with the specified layout result, color and shadow
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
    TextWithShadow(text = "Text")
}