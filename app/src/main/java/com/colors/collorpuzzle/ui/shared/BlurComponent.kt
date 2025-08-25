package com.colors.collorpuzzle.ui.shared

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp


@Composable
fun BlurredBoxComponent(modifier: Modifier) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        BlurForAndroid31AndHigher(modifier)
    } else {
        BlurForAndroidBelow31(modifier)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun BlurForAndroid31AndHigher(modifier: Modifier) {
    Box(
        modifier = modifier
            .graphicsLayer(
                renderEffect = RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.DECAL)
                    .asComposeRenderEffect()
            )
            .background(color = Color.White.copy(alpha = 0.35f))
    )
}

@Composable
private fun BlurForAndroidBelow31(modifier: Modifier) {
    Box(
        modifier = modifier
            .blur(radius = 22.dp)
            .background(Color.White.copy(alpha = 0.35f))

    )
}