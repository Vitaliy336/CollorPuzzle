package com.colors.collorpuzzle.ui.shared.loader

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.data.CellType
import kotlinx.coroutines.delay

private val cubeColors = listOf<CellType>(
    CellType.RED_CELL,
    CellType.GREEN_CELL,
    CellType.BLUE_CELL,
    CellType.YELLOW_CELL
)

@Composable
fun ShowLoader(
    modifier: Modifier = Modifier,
    isFinished: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 96.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .wrapContentWidth()
                .padding(top = 24.dp, bottom = 24.dp, start = 32.dp, end = 32.dp)
                .wrapContentHeight()
        ) {
            var delay = 0
            cubeColors.forEach {
                JumpingBox(delay, it.colorValue)
                delay += 100
            }
        }
        LinearDeterminateIndicator(isFinished = isFinished)
    }
}


@Composable
fun JumpingBox(
    jumpDelay: Int = 0,
    bgColor: Color,
) {
    val offsetY = remember { Animatable(0f) }
    val jumpHeight = (-100).dp // How high the box jumps
    val jumpDurationUp = 400 // Duration of the jump up in milliseconds
    val jumpDurationDown = 300 // Duration of the fall down in milliseconds

    Box(
        modifier = Modifier
            .size(48.dp)
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .clip(shape = RoundedCornerShape(30))
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(30))
            .background(color = bgColor)
    )

    LaunchedEffect(key1 = Unit) {
        delay(jumpDelay.toLong())
        offsetY.animateTo(
            targetValue = jumpHeight.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = jumpDurationUp,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = jumpDurationDown,
                    easing = LinearEasing,
                    delayMillis = jumpDelay.toInt()
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
}

@Preview
@Composable
fun LoaderPreview() {
    ShowLoader(isFinished = {})
}
