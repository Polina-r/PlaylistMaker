package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsearchApi {
    @GET("search?entity=song")
    fun searchSongs(@Query("term") text: String): Call<SearchResponseDto>
}