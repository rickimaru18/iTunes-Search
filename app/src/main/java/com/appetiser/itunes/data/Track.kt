package com.appetiser.itunes.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey
    val trackId: Int,
    val trackName: String?,
    val trackCensoredName: String?,
    val artworkUrl100: String,
    val artistName: String,
    val kind: String?,
    val primaryGenreName: String?,
    val shortDescription: String?,
    val longDescription: String?,
    val currency: String,
    val trackPrice: Double,
    val trackRentalPrice: Double
//    val artworkUrl30: String,
//    val artworkUrl60: String,

//    val collectionArtistId: Int,
//    val collectionArtistViewUrl: String,
//    val collectionCensoredName: String,
//    val collectionExplicitness: String,
//    val collectionHdPrice: Double,
//    val collectionId: Int,
//    val collectionName: String,
//    val collectionPrice: Double,
//    val collectionViewUrl: String,
//    val contentAdvisoryRating: String,
//    val country: String,

//    val discCount: Int,
//    val discNumber: Int,
//    val hasITunesExtras: Boolean,

//    val previewUrl: String,

//    val releaseDate: String,

//    val trackCount: Int,
//    val trackExplicitness: String,
//    val trackHdPrice: Double,
//    val trackHdRentalPrice: Double,

//    val trackNumber: Int,

//    val trackTimeMillis: Int,
//    val trackViewUrl: String,
//    val wrapperType: String
) : RoomEntity {

    var isSelected = false

    @Ignore
    fun clone(): Track {
        val newTrack = Track(
            trackId,
            trackName,
            trackCensoredName,
            artworkUrl100,
            artistName,
            kind,
            primaryGenreName,
            shortDescription,
            longDescription,
            currency,
            trackPrice,
            trackRentalPrice
        )
        newTrack.isSelected = isSelected
        return newTrack
    }

}