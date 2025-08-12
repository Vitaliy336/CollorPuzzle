package com.colors.collorpuzzle.data.local

import kotlinx.coroutines.flow.Flow

interface IPuzzleDataStore {
    suspend fun addClearedLvl(lvlNameStr: String)
    fun getClearedLvlSet(): Set<String>
    fun getClearedLvlFlow(): Flow<Set<String>>
}