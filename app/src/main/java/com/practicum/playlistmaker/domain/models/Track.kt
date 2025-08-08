package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

/*@Parcelize
data class Track (
    @SerializedName("trackId") val trackId: Int,   //ID трека
    @SerializedName("trackName")  val trackName: String, // Название композиции
    @SerializedName("artistName") val artistName: String, // Имя исполнителя
    // val trackTime: String, // Продолжительность трека
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long, // Продолжительность в милисекундах
    @SerializedName("artworkUrl100") val artworkUrl100: String, // Ссылка на изображение обложки
    @SerializedName("collectionName") val collectionName: String?, //Название альбома
    @SerializedName("releaseDate") val releaseDate: String?, //Год релиза трека
    @SerializedName("primaryGenreName") val primaryGenreName: String?, //Жанр трека
    @SerializedName("country") val country: String?, //Страна исполнителя
    @SerializedName("previewUrl") val previewUrl: String?
): Parcelable {
    fun getCoverArtwork(): String {
        return artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")
    }

    fun getReleaseYear(): String? = releaseDate?.take(4)
}*/
@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
): Parcelable {
    fun getCoverArtwork(): String {
        return artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")
    }

    fun getReleaseYear(): String? = releaseDate?.take(4)
}






