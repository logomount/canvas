package com.codescape.canvas.ui.animations

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    sections: Int = 12,
    color: Color = Color.White,
    sectionLength: Dp = 6.dp,
    sectionWidth: Dp = 6.dp
) {

    // Step 1. Create infinite transition to play animation infinitely.
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Step 2. Establish the initial and final position of offset. Animate Int from 0 to number of sections.
    val sectionOffset by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = sections,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "angle_animation"
    )
    Canvas(modifier = modifier) {

        // Step 3. Define rotation radius and angle separation for each section.
        val radius = size.height / 2
        val angle = 360f / sections
        val alpha = 1f / sections

        // Step 4. Rotate the animated angle.
        rotate(sectionOffset * angle) {

            // Step 5. For each section, rotate and draw a line with graduated opacity.
            for (i in 1..sections) {
                rotate(angle * i) {
                    drawLine(
                        color = color.copy(alpha = alpha * i),
                        strokeWidth = sectionWidth.toPx(),
                        start = Offset(
                            x = radius,
                            y = sectionLength.toPx()
                        ),
                        end = Offset(
                            x = radius,
                            y = sectionLength.toPx() * 2
                        ),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0000000
)
@Composable
fun SpinnerPreview() {
    Spinner(modifier = Modifier.size(64.dp))
}