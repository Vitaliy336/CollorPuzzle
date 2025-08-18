package com.colors.collorpuzzle.data

import android.util.Log
import com.colors.collorpuzzle.data.model.Stage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val TAG = "StageConvertor"

fun Stage.convertToJson(gson: Gson): String {
    return gson.toJson(this)
}

fun String.convertToStage(gson: Gson): Stage? {
    val type = object : TypeToken<Stage>() {}.type
    return try {
        gson.fromJson<Stage>(this, type)
    } catch (e: Exception) {
        Log.e(TAG, "convertToStage: ", e)
        null
    }
}