package com.colors.collorpuzzle.ui.shared.control_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R

@Composable
fun RestartComposable(
    modifier: Modifier = Modifier,
    restartClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(all = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_restart), contentDescription = "restart",
            modifier = modifier

                .padding(horizontal = 16.dp, vertical = 4.dp)
                .size(56.dp)
                .clip(shape = CircleShape)
                .border(2.dp, color = Color.Gray, shape = CircleShape)
                .clickable {
                    restartClick()
                }
        )
        Text(text = stringResource(R.string.restart_btn_text))
    }
}


@Composable
@Preview
fun RestartPreview() {
    Surface {
        RestartComposable(
            restartClick = {}
        )
    }
}