package com.colors.collorpuzzle.ui.shared.stage_matrix

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.data.CellType

@Composable
fun ColorToComposable(modifier: Modifier = Modifier, cellType: CellType) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Gray)) { append(text = stringResource(R.string.turn_all_into)) }
            withStyle(style = SpanStyle(color = cellType.color)) { append(text = cellType.colorName) }
        },
        modifier = modifier.padding(
            start = 16.dp, end = 16.dp,
            top = 20.dp, bottom = 0.dp
        )
    )
}