package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

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

       // sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)

        //val isDarkModeEnabled = sharedPreferences.getBoolean("darkMode", false)

        //if (isDarkModeEnabled) {
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        //} else {
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //}

        //val app = applicationContext as App

        //switchTheme = findViewById(R.id.switchTheme)
        //switchTheme.isChecked = app.darkTheme

        //switchTheme.setOnCheckedChangeListener { _, isChecked ->
            //app.switchTheme(isChecked)
       // }

        // Инициализируем Switch после того, как загрузили layout
       // switchTheme = findViewById(R.id.switchTheme)
        //switchTheme.isChecked = isDarkModeEnabled

        // Реализация переключения темы
            //switchTheme.setOnCheckedChangeListener { _, isChecked ->
           // if (isChecked) {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //} else {
               // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
           // }

           // val editor = sharedPreferences.edit()
           // editor.putBoolean("darkMode", isChecked)
            //editor.apply()
        //}

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