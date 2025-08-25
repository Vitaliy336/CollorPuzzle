package com.colors.collorpuzzle.ui.shared.color_selector

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.colors.collorpuzzle.data.CellType
import kotlinx.coroutines.delay

@Composable
fun ColorsPalette(
    modifier: Modifier = Modifier,
    colors: List<CellType> = CellType.getPaletteColors(),
    selectedColor: Int,
    clickListener: (Int) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(20.dp)
            .fillMaxSize()
            .height(8.dp)

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 30.dp, 0.dp, 0.dp, 0.dp)
        ) {
            DrawTriangleShape(color = 0xFFFFFF00, invert = true)
        }
        Spacer(modifier = Modifier.height(8.dp))

        for (color in colors) {
            ColorSelector(
                modifier = modifier,
                cellType = color,
                selectedColor = selectedColor,
                clickListener = clickListener
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, 0.dp, 0.dp, 0.dp)
        ) {
            DrawTriangleShape(color = 0xFFFFFF00, invert = false)
            DrawTriangleShape(color = 0xFFFFFF00, invert = false)
        }
    }
}

@Composable
private fun ColorSelector(
    modifier: Modifier,
    cellType: CellType,
    selectedColor: Int,
    clickListener: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        val isSelected = cellType.colorValue == selectedColor

        Box(
            modifier = modifier
                .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp)
                .size(56.dp)
                .border(
                    2.dp, if (isSelected) Color.Gray
                    else Color.Transparent, CircleShape
                )
                .padding(1.dp)
                .clip(CircleShape)
                .background(color = cellType.color)
                .clickable {
                    clickListener(cellType.colorValue)
                })

        if (isSelected) {
            var expanded by remember { mutableStateOf(false) }
            val offsetX = animateDpAsState(
                targetValue = if (expanded) 8.dp else 0.dp,
                infiniteRepeatable(animation = tween(), repeatMode = RepeatMode.Reverse)
            )

            // 💀double check I bet there is more elegant way
            LaunchedEffect(Unit) {
                delay(100)
                expanded = true
            }

            Box(
                modifier = modifier
                    .height(12.dp)
                    .offset(offsetX.value)
                    .width(12.dp)
                    .drawWithCache {
                        val polygon = RoundedPolygon(
                            numVertices = 3,
                            radius = size.minDimension / 2,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 10f, smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = polygon
                            .toPath()
                            .asComposePath()
                        onDrawBehind {
                            rotate(degrees = 180F) {
                                drawPath(
                                    roundedPolygonPath, color = Color.Gray
                                )
                            }
                        }
                    })
        }
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun ColorsPalettePreview() {
    val mockedPalette = CellType.getPaletteColors()
    ColorsPalette(
        modifier = Modifier,
        colors = mockedPalette,
        selectedColor = 2,
        clickListener = {})
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
private fun ColorsPalettePreviewUnselected() {
    val mockedPalette = CellType.getPaletteColors()
    ColorsPalette(
        modifier = Modifier,
        colors = mockedPalette,
        selectedColor = 0,
        clickListener = {})
}