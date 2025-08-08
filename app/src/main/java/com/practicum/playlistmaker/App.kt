package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme: Boolean = false
        private set

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        // Делаем доступ к App через App.instance
        instance = this

        // Инициализируем зависимости
        Creator.init(this)

        // Читаем сохранённую тему
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean("darkTheme", false)

        applyCurrentTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        // Сохраняем в настройки
        sharedPreferences.edit()
            .putBoolean("darkTheme", darkThemeEnabled)
            .apply()

        applyCurrentTheme()
    }

    private fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        lateinit var instance: App
            private set
    }
}

    /*companion object {
        lateinit var instance: App
            private set
    }

    private lateinit var sharedPreferences: SharedPreferences
    var darkTheme: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean("darkTheme", false)


        applyCurrentTheme()
    }

    private fun applyCurrentTheme() {
        // Лог, чтобы убедиться, что метод вызывается
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        sharedPreferences.edit().putBoolean("darkTheme", darkThemeEnabled).apply()

        applyCurrentTheme()

    }*/
