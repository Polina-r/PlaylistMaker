package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener {
            val searchDisplayIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchDisplayIntent)
        }
        libraryButton.setOnClickListener {
            val libraryDisplayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryDisplayIntent)
        }

        //Во домашнем задании N2 было указано использовать два разных способа и исправить только после проверки, поэтому эта кнопка реализована следующим образом (Toast удален, но есть в предыдущем commit).
        settingsButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val libraryDisplayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(libraryDisplayIntent)
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}