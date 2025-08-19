package com.colors.collorpuzzle.ui.screens.main_menu.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.data.convertToStage
import com.colors.collorpuzzle.data.model.Stage
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TAG = "MainMenuViewModel"

class MainMenuViewModel(
    val repo: RemoteConfigRepo,
    val gson: Gson,
) : ViewModel() {

    private val _stageExportState = MutableStateFlow<PaletteState>(PaletteState.Initial)
    val exportStageState: StateFlow<PaletteState> = _stageExportState

    fun handleUserIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.CustomPalette -> parsePaletteInput(intent.json)
            UserIntent.HandleConfig -> fetchConfig()
        }
    }

    sealed class UserIntent() {
        object HandleConfig : UserIntent()
        data class CustomPalette(val json: String) : UserIntent()
    }

    private fun fetchConfig() {
        repo.initConfigs()
        viewModelScope.launch {
            repo.getConfigState().collect { state ->
                when (state) {
                    is RemoteConfigRepoImpl.ConfigState.Success -> state.config
                    is RemoteConfigRepoImpl.ConfigState.Error -> Log.e(
                        TAG,
                        "fetchConfig: failed to fetch config"
                    )
                    is RemoteConfigRepoImpl.ConfigState.Loading -> {} // add loading later
                }
            }
        }
    }

    private fun parsePaletteInput(json: String) {
        if (json.isEmpty()) {
            _stageExportState.value = PaletteState.Error(R.string.stage_import_empty_palette_error)
        } else {
            val stage: Stage? = json.convertToStage(gson)
            if (stage != null) {
                _stageExportState.value = PaletteState.Success(json)
            } else {
                _stageExportState.value = PaletteState.Error(R.string.stage_import_parse_error)
            }
        }
    }

    sealed class PaletteState {
        object Initial : PaletteState()
        data class Error(val error: Int) : PaletteState()
        data class Success(val json: String) : PaletteState()
    }
}