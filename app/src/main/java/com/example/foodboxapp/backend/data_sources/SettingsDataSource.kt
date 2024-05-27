package com.example.foodboxapp.backend.data_sources

import android.content.SharedPreferences
import androidx.core.content.edit

const val SETTINGS_PREFS_NAME = "app_settings"
interface SettingsDataSource {
    fun edit(update: (SharedPreferences.Editor) -> Unit)
    fun get(key: String, defValue: String = ""): String?
    fun delete(key: String)
}

class SettingsDataSourceImpl(
    private val settingsPrefs: SharedPreferences
): SettingsDataSource {
    override fun edit(update: (SharedPreferences.Editor) -> Unit) {
        settingsPrefs.edit {
            update(this)
        }
    }

    override fun get(key: String, defValue: String): String? {
        return settingsPrefs.getString(key, defValue).takeIf { it?.isNotEmpty() == true }
    }

    override fun delete(key: String) {
        settingsPrefs.edit {
            delete(key)
        }
    }

}