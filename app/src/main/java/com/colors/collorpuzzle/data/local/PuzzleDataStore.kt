package com.colors.collorpuzzle.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


private const val PREF_DATA_STORE_NAME = "PUZZLE_PREFS"

class PuzzleDataStore(appContext: Context) {

    private val gson = Gson()
    private val Context.dataStore by preferencesDataStore(name = PREF_DATA_STORE_NAME)

    private val dataStore: DataStore<Preferences> = appContext.dataStore

    private object PreferencesKeys {
        val CLEARED_LVL_SET = stringPreferencesKey("cleared_levels")
    }

    suspend fun addClearedLvl(lvlNameStr: String) {
        val levels: Set<String> = getClearedLvlSet()
        if (!levels.contains(lvlNameStr)) {
            val mutableSet = mutableSetOf<String>().apply {
                addAll(levels)
                add(lvlNameStr)
            }
            dataStore.edit { prefs ->
                val type = object : TypeToken<Set<String>>() {}.type
                val levelsJson = gson.toJson(mutableSet, type)
                prefs[PreferencesKeys.CLEARED_LVL_SET] = levelsJson
            }
        }
    }

    val clearedLevelsFlow: Flow<Set<String>> = dataStore.data
        .map { prefs ->
            val dataStr = prefs[PreferencesKeys.CLEARED_LVL_SET]
            if (!dataStr.isNullOrEmpty()) {
                val type = object : TypeToken<Set<String>>() {}.type
                gson.fromJson<Set<String>>(dataStr, type)
            } else {
                setOf<String>()
            }
        }

     fun getClearedLvlSet(): Set<String> {
        return runBlocking {
            val clearedLevelsJson: String? = dataStore.data.first()[PreferencesKeys.CLEARED_LVL_SET]
            if (clearedLevelsJson.isNullOrEmpty()) {
                setOf<String>()
            } else {
                val type = object : TypeToken<Set<String>>() {}.type
                gson.fromJson<Set<String>>(clearedLevelsJson, type)
            }
        }
    }
}