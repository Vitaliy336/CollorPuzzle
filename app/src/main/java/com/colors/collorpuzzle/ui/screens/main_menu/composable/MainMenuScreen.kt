package com.colors.collorpuzzle.ui.screens.main_menu.composable

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.screens.main_menu.view_model.MainMenuViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShowMainMenu(
    modifier: Modifier,
    playClicked: () -> Unit,
    randomStageClicked: () -> Unit,
    constructorClicked: () -> Unit,
) {

    val vm = koinViewModel<MainMenuViewModel>()
    vm.handleUserIntent(MainMenuViewModel.UserIntent.HandleConfig)
    val exportState = vm.exportStageState.collectAsState()
    var showExportDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.main_menu),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            val btnModifier: Modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(start = 200.dp, end = 200.dp, top = 8.dp, bottom = 8.dp)
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.start_game),
                playClicked
            )
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.random_mode),
                randomStageClicked
            )
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.constructor_mode),
                constructorClicked
            )
            StageImportEditText(playClick = { json ->
                vm.handleUserIntent(MainMenuViewModel.UserIntent.CustomPalette(json = json))
                showExportDialog = true
            })
        }
        if (showExportDialog) {
            showExportDialog = false
            val currentState = exportState.value
            when (currentState) {
                MainMenuViewModel.PaletteState.Initial -> {} // can be skipped
                is MainMenuViewModel.PaletteState.Error -> {
                    Toast.makeText(context, stringResource(currentState.error), Toast.LENGTH_SHORT)
                        .show()
                }

                is MainMenuViewModel.PaletteState.Success -> {
                    // proceed to palette screen
                }
            }
        }
    }
}

@Composable
private fun StageImportEditText(
    modifier: Modifier = Modifier,
    playClick: (String) -> Unit,
) {
    val text = remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(end = 200.dp, start = 200.dp, top = 8.dp, bottom = 8.dp)
    ) {

        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            placeholder = { Text(text = stringResource(R.string.palette_import_label)) },
            shape = RoundedCornerShape(percent = 48),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            modifier = modifier
                .height(56.dp)
                .weight(90f)
        )

        Image(
            imageVector = Icons.Default.Clear,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                .clip(shape = CircleShape)
                .weight(10f)
                .clickable(onClick = {
                    text.value = ""
                }),
            contentDescription = "clear"
        )

        Image(
            imageVector = Icons.Default.PlayArrow,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
                .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                .clip(shape = CircleShape)
                .weight(10f)
                .clickable(onClick = {
                    playClick(text.value)
                }),
            contentDescription = "play"
        )
    }
}

@Composable
private fun MenuButton(
    modifier: Modifier,
    text: String,
    click: () -> Unit,
) {
    OutlinedButton(
        onClick = click,
        modifier = modifier
    ) {
        Text(text = text)
    }
}


@Preview
@Composable
private fun ButtonPreview() {
    MenuButton(
        modifier = Modifier,
        "Some Text",
        {})
}