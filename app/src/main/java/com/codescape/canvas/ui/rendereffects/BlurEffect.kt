package com.codescape.canvas.ui.rendereffects

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codescape.canvas.R

// Image by David Yu from Pixabay: https://pixabay.com/photos/lines-symmetry-art-architecture-3659995/

@Composable
fun BlurEffectComponent() {
    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {

        // Step 1. Add an Image composable to show as background. Force the image to use its original size.
        Image(
            painter = painterResource(id = R.drawable.lines),
            contentScale = FixedScale(1f),
            contentDescription = "Background"
        )

        // Step 2. Add another Image composable to show as a blur card.  Force the image to use its original size.
        Image(
            modifier = Modifier
                .padding(48.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .graphicsLayer {
                    // Step 3. Add a blur effect graphic layer. Define horizontal and vertical blur radius.
                    renderEffect = BlurEffect(
                        radiusX = 4.dp.toPx(),
                        radiusY = 4.dp.toPx()
                    )
                    // Step 4. Set the shape of the graphic layer.
                    shape = RoundedCornerShape(24.dp)
                    // Step 5. Clip the content to the defined shape.
                    clip = true
                },
            painter = painterResource(id = R.drawable.lines),
            contentScale = FixedScale(1f),
            contentDescription = "Blur Card"
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun BlurEffectComponentPreview() {
    BlurEffectComponent()
}
