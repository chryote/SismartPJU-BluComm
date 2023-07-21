package com.rifqipadisiliwangi.sismartpju.data.utils

import android.content.Context

object SharedPrefsUtil {
    private const val PREFS_NAME = "MyPrefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_REFRESH_TOKEN = "refreshToken"
    private const val USERNAME = "username"

    fun saveUsername(context: Context, phoneNumber: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(USERNAME, phoneNumber)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USERNAME, null)
    }

    fun clearUsername(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(USERNAME)
        editor.apply()
    }

    fun saveTokens(context: Context, token: String, refreshToken: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getTokens(context: Context): Pair<String?, String?> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(KEY_TOKEN, null)
        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        return Pair(token, refreshToken)
    }
}