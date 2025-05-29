package com.colors.collorpuzzle.ui.shared.color_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DrawTriangleShape(color: Long, invert: Boolean) {
    val triangleShape = GenericShape { size, _ ->

        if (invert) {
            moveTo(size.width / 2, size.height)
            lineTo(0f, 0f)
            lineTo(size.width, 0f)
        } else {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)

        }
    }

    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(triangleShape)
            .background(color = Color(color))
    )
}

@Preview
@Composable
fun TrianglePreview(color: Long = 0xFFFFFF00) {
    DrawTriangleShape(color = color, false)
}

@Preview
@Composable
fun TrianglePreviewInverted(color: Long = 0xFFFFFF00) {
    DrawTriangleShape(color = color, true)
}