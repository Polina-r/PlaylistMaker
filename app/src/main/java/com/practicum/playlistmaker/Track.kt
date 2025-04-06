package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Track (
    @SerializedName("trackId") val trackId: Int,   //ID трека
    @SerializedName("trackName")  val trackName: String, // Название композиции
    @SerializedName("artistName") val artistName: String, // Имя исполнителя
                                  //val trackTime: String, // Продолжительность трека
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long, // Продолжительность в милисекундах
    @SerializedName("artworkUrl100") val artworkUrl100: String // Ссылка на изображение обложки
    )






