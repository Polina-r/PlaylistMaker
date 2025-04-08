package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {


    companion object {
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

    }
}