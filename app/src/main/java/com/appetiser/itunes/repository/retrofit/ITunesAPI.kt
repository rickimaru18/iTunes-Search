package com.appetiser.itunes.repository.retrofit

import com.appetiser.itunes.data.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {

    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("country") country: String,
        @Query("media") media: String
    ): SearchResult

}