package com.codescape.canvas.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

const val SVG_PATH = "m0,205.49825l216.31496,0l66.84299,-205.49825l66.84302,205.49825l216.31492,0l-175.00216,127.00345l66.84644,205.49825l-175.00223,-127.00691l-175.00219,127.00691l66.84646,-205.49825l-175.00221,-127.00345z"

@Composable
fun PathAnimation() {

    // Step 1. Check if the current composition is being inspected (preview mode).
    val isPreview = LocalInspectionMode.current

    // Step 2. Create infinite transition to play animation infinitely.
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Step 3. Indefinitely animate float from 0f to 1f. If in preview mode, skip the animation.
    val pathProgress by infiniteTransition.animateFloat(
        initialValue = if (isPreview) 1f else 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5.seconds.toInt(DurationUnit.MILLISECONDS),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "path_progress"
    )

    // Step 4. Read the path from SVG string and create a Path.
    val path = remember { PathParser().parsePathString(pathData = SVG_PATH).toPath() }

    // Step 5. Create PathMeasure.
    val pathMeasure = remember { PathMeasure() }

    // Step 6. Check the size of the Path.
    val size = LocalDensity.current.run { path.getBounds().maxDimension.toDp() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {

            // Step 7. Create a new Path.
            val animatedPath = Path()

            // Step 8. Set Path to PathMeasure.
            pathMeasure.setPath(path, false)

            // Step 9. Get a segment (part) of the path based on the progress of animation.
            pathMeasure.getSegment(
                0f,
                pathProgress * pathMeasure.length,
                animatedPath
            )

            // Step 10. Draw our new animated Path.
            drawPath(
                path = animatedPath,
                brush = Brush.linearGradient(listOf(Color.Red, Color.Blue)),
                style = Stroke(
                    width = 14.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
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
fun PathAnimationComponentPreview() {
    PathAnimation()
}