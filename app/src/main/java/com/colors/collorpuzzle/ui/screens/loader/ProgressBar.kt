package com.colors.collorpuzzle.ui.screens.loader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LinearDeterminateIndicator(isFinished: () -> Unit) {
    var currentProgress = remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp, end = 48.dp)
    ) {

        LaunchedEffect(key1 = Unit) {
            scope.launch {
                loadProgress { progress ->
                    currentProgress.floatValue = progress
                }
                isFinished()
            }
        }
    }

    LinearProgressIndicator(
        progress = currentProgress.floatValue,
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp),
    )
}


// just simulate loading
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress((i.toFloat() / 100) * 2)
        delay(10)
    }
}
