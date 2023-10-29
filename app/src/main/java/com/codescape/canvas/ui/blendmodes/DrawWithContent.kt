package com.codescape.canvas.ui.blendmodes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplaneTicket
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codescape.canvas.R

// Image by Quang Nguyen vinh from Pixabay: https://pixabay.com/photos/palm-trees-coconut-trees-tropical-3058728/

@Composable
fun DrawWithContent() {

    // Step 1. Create an infinite transition with desired label
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Step 2. Animate vertical gradient offset using defined infinite transition
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_progress"
    )

    // Step 3. Animate scale changes for the image
    val imageScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 5000
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "indicator_scale"
    )

    // Step 4. Create a Linear Gradient Brush with dynamic offsets dependent on the infinite transition
    val brush = remember(gradientOffset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * gradientOffset
                val heightOffset = size.height * gradientOffset
                return LinearGradientShader(
                    colors =  listOf(Color.Yellow, Color.Blue, Color.Yellow),
                    from = Offset(
                        x = widthOffset,
                        y = heightOffset + size.height
                    ),
                    to = Offset(
                        x = widthOffset,
                        y = heightOffset
                    ),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    // Step 5. Load image resource that will have applied animations and effects
    val image = ImageBitmap.imageResource(id = R.drawable.palms)

    // Step 6. Draw a Box with nested content including the image, gradient effect and design elements
    Box(
        modifier = Modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                scale(imageScale) {
                    drawImage(image = image, blendMode = BlendMode.SrcIn)
                }
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.SrcAtop,
                    alpha = 0.5f
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(148.dp),
                imageVector = Icons.Rounded.AirplaneTicket,
                contentDescription = "Place"
            )
            Text(
                text = "Travel",
                fontSize = 100.sp,
                fontWeight = FontWeight.W900
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun DrawWithContentPreview() {
    DrawWithContent()
}