package com.colors.collorpuzzle.ui.screens.stage_constructor.stage_constructor

import android.content.ClipData
import android.os.PersistableBundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.data.CellType
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.ConstructorData
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.ConstructorIntent
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.ConstructorIntent.PaletteClick
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.ConstructorIntent.SelectColorToFillPalette
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.StageConstructorViewModel
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.StageConstructorViewModel.ExportState
import com.colors.collorpuzzle.ui.shared.color_selector.ColorsPalette
import com.colors.collorpuzzle.ui.shared.control_components.ImageButtonWithTextComposable
import com.colors.collorpuzzle.ui.shared.stage_matrix.BuildStageMatrix
import org.koin.compose.viewmodel.koinViewModel

const val customPalette = "custom_palette"

@Composable
fun StageConstructorScreen(backClick: () -> Unit) {
    val vm: StageConstructorViewModel = koinViewModel<StageConstructorViewModel>()

    val shouldShowDialog = rememberSaveable { mutableStateOf(false) }
    val showExportDialog = rememberSaveable { mutableStateOf(false) }
    val constructorState = vm.constructorStateFlow.collectAsState()
    val exportState = vm.exportState.collectAsState()

    ConstructorTemplate(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        constructorData = constructorState.value,
        colorSelectorClick = { color ->
            vm.handleIntent(ConstructorIntent.UpdateSelectedColor(color))
        },
        resetPaletteClick = {
            vm.handleIntent(ConstructorIntent.ResetConstructor)
        },
        colorToFillPaletteClick = {
            shouldShowDialog.value = true
        },
        paletteClick = { x, y, cellColor ->
            vm.handleIntent(PaletteClick(x, y, cellColor))
        },
        backClick = backClick,
        exportStageClick = {
            vm.handleIntent(ConstructorIntent.ExportStage)
            showExportDialog.value = true
        }
    )

    if (showExportDialog.value) {
        ShowExportDialog(
            exportState.value,
            closeDialogClick = {
                showExportDialog.value = false
            })
    }

    if (shouldShowDialog.value) {
        ShowColorPickerDialog(
            selectedColor = constructorState.value.colorToFillPalette,
            dismissRequest = {
                shouldShowDialog.value = false
            },
            confirmRequest = { color ->
                vm.handleIntent(SelectColorToFillPalette(color))
                shouldShowDialog.value = false
            }
        )
    }
}

@Composable
private fun ShowExportDialog(
    value: ExportState,
    closeDialogClick: () -> Unit,
) {
    when (value) {
        ExportState.Initial -> {} // do nothing
        is ExportState.Error -> {
            ExportDialog(
                msg = stringResource(value.error),
                btnText = stringResource(R.string.export_stage_ok),
                dismissRequest = closeDialogClick
            )
        }

        is ExportState.Success -> {
            CopyStageJsonToClipBoard(value.json)
            ExportDialog(
                msg = stringResource(R.string.stage_export_success),
                btnText = stringResource(R.string.export_stage_close),
                dismissRequest = closeDialogClick
            )
        }
    }
}

@Composable
private fun CopyStageJsonToClipBoard(stageJson: String) {
    val localClipboardManager = LocalClipboardManager.current
    val clipData = ClipData.newPlainText(customPalette, stageJson)
        .apply {
            description.extras = PersistableBundle().apply {
                putBoolean("android.content.extra.IS_SENSITIVE", true)
            }
        }
    localClipboardManager.setClip(ClipEntry(clipData = clipData))

}

@Composable
private fun ShowColorPickerDialog(
    selectedColor: Int,
    confirmRequest: (Int) -> Unit,
    dismissRequest: () -> Unit,
) {
    ColorToFillPalettePicker(
        previousSelectedColor = selectedColor,
        confirmRequest = confirmRequest,
        dismissRequest = dismissRequest
    )
}

@Composable
private fun ConstructorTemplate(
    modifier: Modifier = Modifier,
    constructorData: ConstructorData,
    colorToFillPaletteClick: () -> Unit,
    colorSelectorClick: (Int) -> Unit,
    paletteClick: (x: Int, y: Int, cellColor: Int) -> Unit,
    resetPaletteClick: () -> Unit,
    backClick: () -> Unit,
    exportStageClick: () -> Unit,
) {

    Row(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxHeight()
                .weight(15f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BackButton(
                    modifier = Modifier
                        .align(alignment = Alignment.Start)
                        .padding(all = 8.dp),
                    backClick = {
                        backClick.invoke()
                    }
                )
                ImageButtonWithTextComposable(
                    modifier = Modifier,
                    componentText = stringResource(R.string.save_stage),
                    painter = painterResource(R.drawable.ic_save),
                    buttonClick = {
                        exportStageClick.invoke()
                    }
                )
                ColorToFillPaletteSelector(
                    modifier = Modifier,
                    colorToFillPalette = constructorData.colorToFillPalette,
                    colorSelectorClick = { colorToFillPaletteClick.invoke() }
                )
                ImageButtonWithTextComposable(
                    modifier = Modifier,
                    componentText = stringResource(R.string.constructor_reset_palette),
                    painter = painterResource(R.drawable.ic_restart),
                    buttonClick = {
                        resetPaletteClick.invoke()
                    }
                )
            }
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8))
                .weight(70f)
        ) {
            PaletteConstructor(
                modifier = Modifier,
                matrix = constructorData.matrix,
                cellClick = { x, y, color ->
                    paletteClick(x, y, color)
                })
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(15f)
        ) {
            ColorsPalette(
                modifier = Modifier,
                selectedColor = constructorData.selectedColor,
                clickListener = { color ->
                    colorSelectorClick(color)
                })
        }
    }
}


@Composable
fun BackButton(
    backClick: () -> Unit,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.close),
        contentDescription = "back",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(32.dp)
            .clickable {
                backClick.invoke()
            }
    )
}

@Composable
private fun ColorToFillPaletteSelector(
    modifier: Modifier = Modifier,
    colorToFillPalette: Int,
    colorSelectorClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentSize()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Box(
            modifier = modifier
                .size(48.dp)
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20f)
                )
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20f))
                .background(color = CellType.getCellColor(colorToFillPalette).color)
                .clickable(onClick = {
                    colorSelectorClick.invoke()
                })
        )
        Text(
            text = stringResource(R.string.constructor_color_to_fill_selected),
            modifier = modifier.padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PaletteConstructor(
    modifier: Modifier,
    matrix: Matrix,
    cellClick: (x: Int, y: Int, color: Int) -> Unit,
) {
    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .padding(
                start = 16.dp, end = 16.dp,
                bottom = 0.dp,
                top = 0.dp
            ),
        contentAlignment = Alignment.Center,
    ) {
        val boxWithConstraintsScope = this
        BuildStageMatrix(
            modifier = modifier,
            boxWithConstraintsScope.maxWidth,
            boxWithConstraintsScope.minHeight,
            matrix,
            cellClick = cellClick
        )
    }
}

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun ConstructorPreview() {

    val initialConstructorMatrix by lazy {
        Array(8) { IntArray(10) { CellType.EMPTY_CELL.colorValue } } // default matrix 8 rows and 10 columns maybe add custom sizes in future
    }

    ConstructorTemplate(
        modifier = Modifier,
        constructorData = ConstructorData(
            matrix = initialConstructorMatrix,
            colorToFillPalette = CellType.GREEN_CELL.colorValue,
            selectedColor = CellType.EMPTY_CELL.colorValue
        ),
        colorSelectorClick = {},
        resetPaletteClick = {},
        colorToFillPaletteClick = {},
        paletteClick = { x, y, color -> },
        backClick = {},
        exportStageClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ColorToFillPalettePreview() {
    ColorToFillPaletteSelector(
        modifier = Modifier,
        colorToFillPalette = 1,
        colorSelectorClick = { 1 })
}

@Preview(showBackground = true)
@Composable
fun ShowLeftPanel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackButton({}, Modifier.align(alignment = Alignment.Start))
        ImageButtonWithTextComposable(
            modifier = Modifier,
            componentText = stringResource(R.string.save_stage),
            painter = painterResource(R.drawable.ic_save),
            buttonClick = {}
        )
        ColorToFillPaletteSelector(
            modifier = Modifier,
            colorToFillPalette = 2,
            colorSelectorClick = { }
        )
        ImageButtonWithTextComposable(
            modifier = Modifier,
            componentText = stringResource(R.string.constructor_reset_palette),
            painter = painterResource(R.drawable.ic_restart),
            buttonClick = {}
        )
    }
}