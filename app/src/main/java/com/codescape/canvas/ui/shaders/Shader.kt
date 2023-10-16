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
    val runtimeShader = remember(shaderScript) { RuntimeShader(shaderScript) }
    val shaderBrush = remember(runtimeShader) { ShaderBrush(runtimeShader) }
    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameNanos { frameTimeNanos ->
                value = frameTimeNanos / 1000000000f
            }
        }
    }
    var size by remember { mutableStateOf(Size(0f, 0f)) }
    var position by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures {
                    position = Offset(
                        x = it.x,
                        y = size.height - it.y
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    position = Offset(
                        x = position.x + dragAmount.x,
                        y = position.y - dragAmount.y
                    )
                }
            }
            .onGloballyPositioned {
                size = Size(
                    width = it.size.width.toFloat(),
                    height = it.size.height.toFloat()
                )
            }
            .drawBehind {
                drawRect(Color.Black)

                // Set global constants for the Shader
                runtimeShader.setFloatUniform("iResolution", size.width, size.height)
                runtimeShader.setFloatUniform("iTime", time)
                runtimeShader.setFloatUniform("iMouse", position.x, position.y)

                // AGSL to GLSL coordinate space transformation matrix
                val localMatrix = Matrix()
                localMatrix.postScale(1.0f, -1.0f)
                localMatrix.postTranslate(0.0f, size.height)
                runtimeShader.setLocalMatrix(localMatrix)

                // Draw shader
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