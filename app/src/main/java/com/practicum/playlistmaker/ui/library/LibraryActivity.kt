package com.practicum.playlistmaker.ui.library

import android.content.Context
//import android.icu.text.SimpleDateFormat
//import android.icu.util.TimeZone
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.Locale
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.os.Handler
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.TimeZone


class LibraryActivity : AppCompatActivity() {

    private lateinit var track: Track

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var playPauseButton: ImageButton
    private lateinit var playbackTimeTextView: TextView
    private var handler: Handler = Handler(Looper.getMainLooper())


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            /*enableEdgeToEdge()*/
            setContentView(R.layout.activity_library)

            playbackTimeTextView = findViewById(R.id.playbackTime)


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

            //Кнопки
        playPauseButton = findViewById(R.id.playPauseButton)
        playbackTimeTextView = findViewById(R.id.playbackTime)

        playPauseButton.setOnClickListener {
            handlePlayPause()
        }

        playbackTimeTextView.text = formatTrackTime(0)
        playbackTimeTextView.visibility = View.VISIBLE
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

    private fun handlePlayPause() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(track.previewUrl)
                prepare()
                setOnCompletionListener {
                    this@LibraryActivity.isPlaying = false
                    playPauseButton.setImageResource(R.drawable.button_play)
                    playbackTimeTextView.text = "00:00"
                    handler.removeCallbacksAndMessages(null)
                }
            }
        }

        if (isPlaying) {
            mediaPlayer?.pause()
            playPauseButton.setImageResource(R.drawable.button_play)
            handler.removeCallbacksAndMessages(null)
            //playbackTimeTextView.visibility = View.GONE
        } else {
            if (mediaPlayer?.currentPosition == mediaPlayer?.duration) {
                mediaPlayer?.seekTo(0)
            }

            mediaPlayer?.start()
            playPauseButton.setImageResource(R.drawable.button_pause)

            //playbackTimeTextView.text = formatTrackTime(mediaPlayer?.currentPosition?.toLong() ?: 0L)
            val currentPos = mediaPlayer?.currentPosition?.toLong() ?: 0L
            playbackTimeTextView.text = formatTrackTime(currentPos)

            updatePlaybackTime()
        }

        isPlaying = !isPlaying
    }

    private fun updatePlaybackTime() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    val currentPosition = it.currentPosition
                    playbackTimeTextView.text = formatTrackTime(currentPosition.toLong())
                    if (isPlaying) {
                        handler.postDelayed(this, 500)
                    }
                }
            }
        }, 1000)
    }

    override fun onBackPressed() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
        isPlaying = false
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (isPlaying) {
            mediaPlayer?.pause()
            playPauseButton.setImageResource(R.drawable.button_play)
            handler.removeCallbacksAndMessages(null)
            isPlaying = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}


