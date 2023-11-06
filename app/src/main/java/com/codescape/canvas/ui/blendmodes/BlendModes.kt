package com.codescape.canvas.ui.blendmodes

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

data class PieSlice(
    val color: Color,
    @FloatRange(from = 0.0, to = 1.0)
    val size: Float,
    val name: String
)

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>,
    strokeWidth: Dp = 24.dp,
    dividerWidth: Dp = 24.dp,
    animationDuration: Int = 1000
) {

    // Step 1. Check if the current composition is being inspected (preview mode).
    val isPreview = LocalInspectionMode.current

    // Step 2. Remember the animated angles for each slice. If in preview mode, skip the animation.
    val animatedSweepAngles = remember(slices) {
        slices.map { Animatable(if (isPreview) it.size * 360 else 0f) }
    }

    // Step 3. Animate the sweep angles to their final positions when the composition is launched.
    LaunchedEffect(animatedSweepAngles) {
        slices.forEachIndexed { index, slice ->
            animatedSweepAngles[index].animateTo(
                targetValue = slice.size * 360,
                animationSpec = tween(animationDuration)
            )
        }
    }

    // Step 4. Draw the pie chart on a Canvas, with an aspect ratio of 1:1 (circle).
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {

        // Step 5. Calculate the radius of the pie chart (half the minimum dimension of the Canvas).
        val radius = size.minDimension / 2
        var sliceAngle = 0f

        // Step 6. For each slice, draw an arc of the pie chart with the appropriate color and size.
        for ((index, slice) in slices.withIndex()) {
            drawArc(
                color = slice.color,
                startAngle = sliceAngle,
                sweepAngle = animatedSweepAngles[index].value,
                useCenter = false,
                topLeft = center - Offset(
                    radius - strokeWidth.toPx() / 2,
                    radius - strokeWidth.toPx() / 2
                ),
                size = size.copy(
                    width = size.width - strokeWidth.toPx(),
                    height = size.height - strokeWidth.toPx()
                ),
                style = Stroke(width = strokeWidth.toPx())
            )
            sliceAngle += animatedSweepAngles[index].value
        }

        // Step 7. Draw dividers for each slice to clearly separate them.
        var spacerAngle = 0f
        for (slice in slices) {
            val angleRadian = Math.toRadians(spacerAngle.toDouble())
            val endX = center.x + radius * cos(angleRadian).toFloat()
            val endY = center.y + radius * sin(angleRadian).toFloat()
            drawLine(
                color = Color.Black,
                start = Offset(center.x, center.y),
                end = Offset(endX, endY),
                strokeWidth = dividerWidth.toPx(),
                blendMode = BlendMode.DstOut
            )
            spacerAngle += slice.size * 360
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PieChartPreview() {
    val slices = listOf(
        PieSlice(
            color = Color.Red,
            size = 0.5f,
            name = "first"
        ),
        PieSlice(
            color = Color.Blue,
            size = 0.3f,
            name = "two"
        ),
        PieSlice(
            color = Color.Green,
            size = 0.2f,
            name = "three"
        )
    )
    PieChart(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(24.dp),
        slices = slices,
        strokeWidth = 64.dp
    )
}