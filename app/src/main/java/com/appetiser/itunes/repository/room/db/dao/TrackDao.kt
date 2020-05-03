package com.appetiser.itunes.repository.room.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.appetiser.itunes.data.Track

@Dao
interface TrackDao {

    @Query("SELECT * FROM tracks")
    fun getTracks(): DataSource.Factory<Int, Track?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tracks: List<Track>)

    @Update
    fun update(track: Track)

    @Query("DELETE FROM tracks")
    fun deleteAll()

}