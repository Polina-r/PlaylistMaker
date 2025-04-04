package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    var searchText: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private val trackList = mutableListOf<Track>()
    lateinit var songSearchApi: SongsearchApi
    private lateinit var queryInput: EditText

    private lateinit var noResultsPlaceholder: LinearLayout
    private lateinit var serverErrorPlaceholder: LinearLayout
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.activity_search)


        songSearchApi = RetrofitClient.retrofit.create(SongsearchApi::class.java)

        queryInput = findViewById(R.id.searchEditText)

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = queryInput.text.toString()
                searchTracks(query)
                true
            } else {
                false
            }
        }

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
            clearButton.visibility = View.GONE // Скрытие кнопки очистки
            recyclerView.visibility = View.GONE // Скрытие списка треков
            noResultsPlaceholder.visibility = View.GONE // Скрытие заглушки "Нет результатов"
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
                // TODO заглушка
            }
        })

        //Список треков RecyclerView
        recyclerView = findViewById(R.id.recyclerView)

        //trackList.add(Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"))
        //trackList.add(Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"))
        //trackList.add(Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"))
        //trackList.add(Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"))
        //trackList.add(Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"))

        trackAdapter = TrackAdapter(trackList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        //Placeholder
        noResultsPlaceholder = findViewById(R.id.noResultsPlaceholder)
        serverErrorPlaceholder = findViewById(R.id.serverErrorPlaceholder)
        retryButton = findViewById(R.id.retryButton)

        retryButton.setOnClickListener {
            val query = queryInput.text.toString()
            searchTracks(query)
        }
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

    // Логика выполнения поискового запроса

private fun searchTracks(query: String) {

    val call = songSearchApi.searchSongs(query)
    call.enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
             if (response.isSuccessful) {
                val tracks = response.body()?.results ?: emptyList()
                if (tracks.isEmpty()) {
                    showNoResultsPlaceholder()
                } else {
                    trackList.clear()
                    trackList.addAll(tracks)
                    trackAdapter.notifyDataSetChanged()
                    hidePlaceholders()
                }
            } else {
                showErrorPlaceholder()
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            showErrorPlaceholder()
        }
    })
}
    //Функция Placeholder serverError
    private fun showErrorPlaceholder() {
        hideKeyboard(queryInput)
        findViewById<LinearLayout>(R.id.noResultsPlaceholder).visibility = View.GONE
        findViewById<LinearLayout>(R.id.serverErrorPlaceholder).visibility = View.VISIBLE
        findViewById<RecyclerView>(R.id.recyclerView).visibility = View.GONE
    }

    //Функция нет результатов
    private fun showNoResultsPlaceholder() {
        noResultsPlaceholder.visibility = View.VISIBLE
        serverErrorPlaceholder.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }
//Функция отображения RecyclerView
    private fun hidePlaceholders() {
        noResultsPlaceholder.visibility = View.GONE
        serverErrorPlaceholder.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun formatDuration(ms: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(ms)
    }
}




