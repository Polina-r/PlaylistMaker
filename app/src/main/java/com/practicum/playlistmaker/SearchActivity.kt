package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.internal.ViewUtils.hideKeyboard

class SearchActivity : AppCompatActivity() {

    var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.activity_search)


        // Toolbar "Поиск" и кнопка Назад
        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }



        // Поисковая строка
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString("search_text")
            searchEditText.setText(searchText)
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            hideKeyboard(searchEditText)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                    // TODO заглушка
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO заглушка
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s?.toString()
            }
        })

    }

    // Функция убрать клавиатуру
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Функция сохранения текста
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search_text", searchText)
    }

    // Функция восстановления текста
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("search_text")
    }

}
