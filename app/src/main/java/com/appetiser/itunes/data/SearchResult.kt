package com.appetiser.itunes.data

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val resultCount: Int,
    @SerializedName("results")
    val tracks: List<Track>
)