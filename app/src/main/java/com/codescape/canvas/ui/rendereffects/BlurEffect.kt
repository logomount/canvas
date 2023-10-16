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
    Icons.Default.ArrowBack.root.clipPathData
    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.lines),
            contentScale = FixedScale(1f),
            contentDescription = "Background"
        )
        Image(
            modifier = Modifier
                .padding(48.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(24.dp)
                )
                .graphicsLayer {
                    renderEffect = BlurEffect(
                        radiusX = 4.dp.toPx(),
                        radiusY = 4.dp.toPx()
                    )
                    shape = RoundedCornerShape(24.dp)
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
