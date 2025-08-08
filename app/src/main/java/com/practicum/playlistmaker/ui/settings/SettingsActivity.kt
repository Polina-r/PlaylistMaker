package com.practicum.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity: AppCompatActivity() {

    private lateinit var switchTheme: SwitchMaterial
    //private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        switchTheme = findViewById(R.id.switchTheme)
        Log.d("AppTheme", "SettingsActivity onCreate: darkTheme: ${App.instance.darkTheme}")
        switchTheme.isChecked = App.instance.darkTheme

        switchTheme.setOnCheckedChangeListener { _, checked ->
            Log.d("AppTheme", "Switch checked: $checked")
            App.instance.switchTheme(checked)
        }

        // Кнопка "Назад"
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Кнопка "Поделиться приложением"
        val shareButton = findViewById<MaterialTextView>(R.id.shareButton)
        shareButton.setOnClickListener {
            val message = getString(R.string.course_link)
            shareText(message)
        }

        // Кнопка "Написать в поддержку"
        val supportButton = findViewById<MaterialTextView>(R.id.supportButton)
        supportButton.setOnClickListener {
            writeToSupport()
        }

        // Кнопка "Пользовательское соглашение"
        val userAgreementButton = findViewById<MaterialTextView>(R.id.userAgreementButton)
        userAgreementButton.setOnClickListener {
            openAgreement()
        }
    }

    // Функция "Поделиться приложением"
    private fun shareText(message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.app_share)))
    }

    // Функция "Написать в поддержку"
    private fun writeToSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("pfayzulaeva@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
        }
        startActivity(Intent.createChooser(supportIntent, "Отправить сообщение"))
    }

    // Функция "Пользовательское соглашение"
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