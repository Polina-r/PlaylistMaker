package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity: AppCompatActivity() {

    private lateinit var switchTheme: SwitchMaterial
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.activity_settings)

        this.sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)

        //Switch
        this.switchTheme = findViewById(R.id.switchTheme)
        //switchTheme.isChecked = false

        //Кнопка "Назад"
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //Реализация переключения темы
        val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)
        switchTheme.isChecked = isDarkModeEnabled

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            val editor = sharedPreferences.edit()
            editor.putBoolean("darkMode", isChecked)
            editor.apply()
        }

        //Кнопка "Поделиться приложением"
            val shareButton = findViewById<MaterialTextView>(R.id.shareButton)

        shareButton.setOnClickListener {
            val message = getString(R.string.course_link)
            shareText(message)
       }

        //Кнопка "Написать в поддержку"
        val supportButton = findViewById<MaterialTextView>(R.id.supportButton)
        supportButton.setOnClickListener {
            writeToSupport()
        }

        //Кнопка "Пользовательское соглашение"
        val userAgreementButton = findViewById<MaterialTextView>(R.id.userAgreementButton)
        userAgreementButton.setOnClickListener {
            openAgreement()
        }
    }


    //Функция "Поделиться приложением"
    private fun shareText(message: String) {
        //Создаём Intent
        val shareIntent = Intent(Intent.ACTION_SEND)

        // Тип данных
        shareIntent.type = "text/plain"

        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.app_share)))
    }

    //Функция "Написать в поддержку"
    private fun writeToSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            // Тип данных
            data = Uri.parse("mailto:")

            putExtra(Intent.EXTRA_EMAIL, arrayOf("pfayzulaeva@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
        }

        startActivity(Intent.createChooser(supportIntent, "Отправить сообщение"))
    }

    //Функция "Пользовательское соглашение"
    private fun openAgreement() {
        val userAgreementUrl = getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(userAgreementUrl))

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.browser_not_found)))
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.browser_not_found), Toast.LENGTH_SHORT).show()
        }
    }
}
