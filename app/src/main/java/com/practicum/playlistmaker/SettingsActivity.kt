package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val settingsReturnButton = findViewById<Button>(R.id.arrow_button)

        settingsReturnButton.setOnClickListener {
            val settingsReturnDisplayIntent = Intent(this, MainActivity::class.java)
            startActivity(settingsReturnDisplayIntent)
        }
    }
}