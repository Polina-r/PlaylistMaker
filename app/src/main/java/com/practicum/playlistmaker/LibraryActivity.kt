package com.practicum.playlistmaker

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.internal.ViewUtils.dpToPx
import java.util.Locale
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class LibraryActivity : AppCompatActivity() {

    private lateinit var track: Track

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            /*enableEdgeToEdge()*/
            setContentView(R.layout.activity_library)

            // Получаем трек
            @Suppress("DEPRECATION")
            track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("track", Track::class.java)
            } else {
                intent.getParcelableExtra("track") as? Track
            } ?: return

            setupUI()

            // Обработчик нажатия кнопки "Назад"
            findViewById<ImageButton>(R.id.backButton).setOnClickListener {
                finish()
            }
        }

    private fun setupUI() {
        // Название трека
        findViewById<TextView>(R.id.trackName).text = track.trackName

        // Имя исполнителя
        findViewById<TextView>(R.id.artistName).text = track.artistName

        // Название альбома (если есть)
        val collectionNameTextView = findViewById<TextView>(R.id.collectionName)
        track.collectionName?.let {
            collectionNameTextView.text = it
            collectionNameTextView.visibility = View.VISIBLE
        } ?: run {
            collectionNameTextView.visibility = View.GONE
        }

        // Год релиза
        val releaseDateTextView = findViewById<TextView>(R.id.releaseDate)
        track.releaseDate?.let {
            // Преобразуем строку даты в год
            val year = extractYearFromDate(it)
            releaseDateTextView.text = year
            releaseDateTextView.visibility = View.VISIBLE
        } ?: run {
            releaseDateTextView.visibility = View.GONE
        }

        // Жанр
        findViewById<TextView>(R.id.primaryGenreName).text = track.primaryGenreName

        // Страна исполнителя
        findViewById<TextView>(R.id.country).text = track.country

        // Время трека
        findViewById<TextView>(R.id.trackTimeMillis).text = formatTrackTime(track.trackTimeMillis)

        // Обложка
        val coverImageView = findViewById<ImageView>(R.id.coverImage)
        val coverUrl = track.getCoverArtwork()

        val context = this
        val cornerRadiusPx = dpToPx(8, context)

        Glide.with(this)
            .load(coverUrl)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerRadiusPx))
            .into(coverImageView)
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
    // Функция для форматирования времени трека в формат mm:ss
    private fun formatTrackTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // Функция для форматирования даты в год
    private fun extractYearFromDate(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = originalFormat.parse(dateString)
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return yearFormat.format(date)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("track", track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Для Android 13 (API 33) и выше
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState.getParcelable("track", Track::class.java)  // Указываем тип
        } else {
            savedInstanceState.getParcelable("track")
        } ?: return

        setupUI()  // Перерисовываем UI
    }
}


