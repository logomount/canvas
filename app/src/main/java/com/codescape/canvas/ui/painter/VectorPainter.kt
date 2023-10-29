package com.codescape.canvas.ui.painter

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.codescape.canvas.R

@Composable
fun VectorPainter(modifier: Modifier = Modifier) {

    // Step 1. Create infinite transition to play animation infinitely.
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Step 2. Animate the scale of the shadow 1f to 1.5.dp indefinitely.
    val backgroundScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_rotation"
    )

    // Step 3. Create VectorPainter from drawable resource.
    val vectorPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.android_robot))

    Canvas(modifier = modifier) {
        with(vectorPainter) {
            // Step 4. Scale the image
            val scale = size.width / vectorPainter.intrinsicSize.width
            val scaledSize = vectorPainter.intrinsicSize * scale / 2f
            inset(
                horizontal = (size.width - scaledSize.width) / 2f,
                vertical = (size.height - scaledSize.height) / 2f
            ) {
                // Step 5. Use VectorPainter DrawScope.draw() function
                scale(backgroundScale) {
                    draw(
                        size = scaledSize,
                        alpha = 0.5f,
                        colorFilter = ColorFilter.tint(color = Color.LightGray)
                    )
                }
                draw(size = scaledSize)
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun IVectorPainterPreview() {
    VectorPainter(modifier = Modifier.fillMaxSize())
}