package com.example.foodboxapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.foodboxapp.backend.repositories.SettingsRepository
import com.example.foodboxapp.di.appModule
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin{
            androidContext(this@App)
            modules(
                listOf(
                    appModule
                )
            )
        }

        val settingsRepository by inject<SettingsRepository>()
        settingsRepository.load()
        CoroutineScope(Dispatchers.Default).launch{
            settingsRepository.state.collectLatest { settings ->

                val currentTheme = AppCompatDelegate.getDefaultNightMode()
                val theme = when (settings.theme.stringRep) {
                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }

                if (currentTheme != theme) {
                    withContext(Dispatchers.Main) {
                        AppCompatDelegate.setDefaultNightMode(theme)
                    }
                }

                val currentLanguage =
                    AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()
                val language = if (settings.language.stringRep != "system") {
                    settings.language.stringRep
                } else {
                    null
                }

                if (currentLanguage != language) {
                    withContext(Dispatchers.Main) {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(
                                language
                            )
                        )
                    }
                }
            }
        }
    }
}