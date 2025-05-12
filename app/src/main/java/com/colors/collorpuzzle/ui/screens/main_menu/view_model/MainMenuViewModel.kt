package com.colors.collorpuzzle.ui.screens.main_menu.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import kotlinx.coroutines.launch

const val TAG = "MainMenuViewModel"

class MainMenuViewModel(val repo: RemoteConfigRepo) : ViewModel() {

    fun test() {
        repo.initConfigs()
        viewModelScope.launch {
            repo.getConfigState().collect { state ->
                when (state) {
                    is RemoteConfigRepoImpl.ConfigState.Success -> state.config
                    is RemoteConfigRepoImpl.ConfigState.Error -> ""
                    else -> ""
                }
            }
        }
        Log.d(TAG, "MainMenuViewModel test")

    }
}