package com.colors.collorpuzzle.data.repo

import com.colors.collorpuzzle.data.model.Stages
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl.ConfigState
import kotlinx.coroutines.flow.StateFlow

interface RemoteConfigRepo {
    fun initConfigs()
    fun getConfigState(): StateFlow<ConfigState>
    fun getConfigs(): List<Stages>
}