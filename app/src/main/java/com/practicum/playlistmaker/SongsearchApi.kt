package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsearchApi {
    @GET("search?entity=song")
    fun searchSongs(@Query("term") text: String): Call<ApiResponse>
}