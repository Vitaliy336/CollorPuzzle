package com.colors.collorpuzzle.data.repo

import android.util.Log
import com.colors.collorpuzzle.data.model.Stages
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "RemoteConfigRepoImpl"
private const val PALETTE_STAGES = "palette_stages"

class RemoteConfigRepoImpl : RemoteConfigRepo {

    private val gson = Gson()
    private val _configStateFlow = MutableStateFlow<ConfigState>(ConfigState.Loading())
    private val configFlow: StateFlow<ConfigState> = _configStateFlow
    private val config = Firebase.remoteConfig
    private val stages: List<Stages> by lazy {
        val jsonStr = config.getString(PALETTE_STAGES)
        val type = genericType<List<Stages>>()
        gson.fromJson(jsonStr, type)
    }

    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    override fun initConfigs() {
        val cacheInterval = 3000L
        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 20L
            minimumFetchIntervalInSeconds = cacheInterval
        }

        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "initConfigs: ${task.result}")
                    _configStateFlow.value = ConfigState.Success(stages)
                } else {
                    Log.d(TAG, "failed to fetch config")
                    _configStateFlow.value = ConfigState.Error()
                }
            }
    }

    override fun getConfigState(): StateFlow<ConfigState> {
       return configFlow
    }

    override fun getConfigs(): List<Stages> {
        return stages
    }

    sealed class ConfigState {
        class Loading() : ConfigState()
        class Error() : ConfigState()
        class Success(val config: List<Stages>) : ConfigState()
    }
}