package com.appetiser.itunes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class Config(
    var term: String?,
    var media: String,
    var lastUpdated: String
) : RoomEntity {

    @PrimaryKey(autoGenerate = false)
    var id = 1

}