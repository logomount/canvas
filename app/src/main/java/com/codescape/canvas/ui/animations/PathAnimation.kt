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
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")
    val pathProgress by infiniteTransition.animateFloat(
        initialValue = if (LocalInspectionMode.current) 0f else 0f,
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
    val path = remember { PathParser().parsePathString(pathData = SVG_PATH).toPath() }
    val pathMeasure = remember { PathMeasure() }
    val size = LocalDensity.current.run { path.getBounds().maxDimension.toDp() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val animatedPath = Path()
            pathMeasure.setPath(path, false)
            pathMeasure.getSegment(
                0f,
                pathProgress * pathMeasure.length,
                animatedPath
            )
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