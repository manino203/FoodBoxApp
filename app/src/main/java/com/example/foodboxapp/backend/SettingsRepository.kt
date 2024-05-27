package com.example.foodboxapp.backend

import com.example.foodboxapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


interface SettingsRepository {
    val state: StateFlow<SettingsState>
    fun update(newState: SettingsState)
    fun load()
}

class SettingsRepositoryImpl(
    private val dataSource: SettingsDataSource
): SettingsRepository{
    companion object {
        const val KEY_LANGUAGE = "language"
        const val KEY_THEME = "theme"

        val LANGUAGE_CHOICES = listOf(
            SettingsState.Language.System,
            SettingsState.Language.En,
            SettingsState.Language.Sk
        )
        val THEME_CHOICES = listOf(
            SettingsState.Theme.System,
            SettingsState.Theme.Dark,
            SettingsState.Theme.Light
        )

    }

    override val state: StateFlow<SettingsState>
        get() = _state.asStateFlow()
    private val _state = MutableStateFlow(SettingsState())
    override fun update(newState: SettingsState) {
        _state.update {
            newState
        }
        dataSource.edit{
            it.putString(
                KEY_THEME,
                state.value.theme.stringRep
                )
            it.putString(
                KEY_LANGUAGE,
                state.value.language.stringRep
            )
        }
    }

    override fun load() {
        _state.update {
            SettingsState(
                getTheme(),
                getLanguage()
            )
        }
    }

    private fun getLanguage(): SettingsState.Language {
        return getChoiceListSetting(
            KEY_LANGUAGE,
            LANGUAGE_CHOICES,
            SettingsState.Language.System
        )
    }

    private fun getTheme(): SettingsState.Theme {
        return getChoiceListSetting(
            KEY_THEME,
            THEME_CHOICES,
            SettingsState.Theme.System
        )
    }


    private fun <T: SettingsState.ChoiceSetting> getChoiceListSetting(key: String, choices: List<SettingsState.ChoiceSetting>, defValue: T): T{
        @Suppress("UNCHECKED_CAST")
        return (choices.first {
            it.stringRep == dataSource.get(key, SettingsState.Language.System.stringRep)
        } as? T) ?: defValue
    }



}
data class SettingsState(
    val  theme: Theme = Theme.System,
    val  language: Language = Language.System
){
    interface ChoiceSetting {
        val stringRep: String
        val label: Int
    }
    sealed class Theme: ChoiceSetting{
        data object System: Theme(){
            override val stringRep = "system"
            override val label = R.string.settings_system_theme
        }
        data object Dark: Theme(){
            override val stringRep = "dark"
            override val label = R.string.settings_dark_theme

        }
        data object Light: Theme() {
            override val stringRep = "light"
            override val label = R.string.settings_light_theme

        }
    }

    sealed class Language: ChoiceSetting{
        data object System: Language(){
            override val label = R.string.settings_system_language
            override val stringRep = "system"
        }
        data object En: Language(){
            override val label = R.string.settings_english_language
            override val stringRep = "en"
        }
        data object Sk: Language() {
            override val label = R.string.settings_slovak_language
            override val stringRep = "sk"
        }
    }
}