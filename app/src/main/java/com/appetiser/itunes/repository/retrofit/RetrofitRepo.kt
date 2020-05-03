package com.appetiser.itunes.repository.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Repository for iTunes web service API.
 */
class RetrofitRepo {

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    private val _iTunesAPI: ITunesAPI

    constructor() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        _iTunesAPI = retrofit.create(ITunesAPI::class.java)
    }

    /**
     * Search tracks from iTunes.
     *
     * @param term      Track term.
     * @param country   Country.
     * @param media     Media type.
     * @return [SearchResult]
     */
    suspend fun searchTracks(
        term: String,
        country: String,
        media: String
    ) = _iTunesAPI.searchTracks(term, country, media)

}