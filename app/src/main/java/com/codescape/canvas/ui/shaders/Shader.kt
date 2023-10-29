package com.codescape.canvas.ui.shaders

import android.graphics.Matrix
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.withInfiniteAnimationFrameNanos
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import org.intellij.lang.annotations.Language

// Online Shader editor: https://thebookofshaders.com/edit.php

@Language("AGSL")
const val SIMPLE_SHADER =
    """
    uniform float2 iResolution;
    uniform float iTime;
    uniform float2 iMouse;
    half4 main(float2 fragCoord) {
        float2 pct = fragCoord/iResolution.xy;
        float d = distance(pct, iMouse.xy/iResolution.xy);
        return half4(half3(1, 0, 0)*(1-d)*(1-d), 1);
    }
    """

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShaderComponent(shaderScript: String) {
    // Step 1. Create a RuntimeShader.
    val runtimeShader = remember(shaderScript) { RuntimeShader(shaderScript) }

    // Step 2. Create a ShaderBrush from the RuntimeShader.
    val shaderBrush = remember(runtimeShader) { ShaderBrush(runtimeShader) }

    // Step 3. Set an animator to continuously update the time state every second.
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameNanos { frameTimeNanos ->
                value = frameTimeNanos / 1000000000f
            }
        }
    }

    // Step 4. Create a position state to store the position of the touch event.
    var position by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                // Step 5. Add a gesture detector to update the position state on touch events.
                detectTapGestures {
                    position = Offset(
                        x = it.x,
                        y = size.height - it.y
                    )
                }
            }
            .pointerInput(Unit) {
                // Step 6. Add a gesture detector to update the position state when drag events occur.
                detectDragGestures { _, dragAmount ->
                    position = Offset(
                        x = position.x + dragAmount.x,
                        y = position.y - dragAmount.y
                    )
                }
            }
            // Step 7. Update the size state when the component is placed in the Compose hierarchy.
            .drawBehind {
                // Step 8. Set global constants for the Shader.
                runtimeShader.setFloatUniform("iResolution", size.width, size.height)
                runtimeShader.setFloatUniform("iTime", time)
                runtimeShader.setFloatUniform("iMouse", position.x, position.y)

                // Step 9. Transform the AGSL to GLSL coordinate space.
                val localMatrix = Matrix()
                localMatrix.postScale(1.0f, -1.0f)
                localMatrix.postTranslate(0.0f, size.height)
                runtimeShader.setLocalMatrix(localMatrix)

                // Step 10. Draw the shader rect.
                drawRect(brush = shaderBrush)
            }
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun ShaderComponentPreview() {
    ShaderComponent(shaderScript = SIMPLE_SHADER)
}