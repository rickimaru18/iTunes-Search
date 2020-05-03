package com.appetiser.itunes.repository.room.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.appetiser.itunes.data.Config

@Dao
interface ConfigDao {

    @Query("SELECT * FROM config")
    fun getConfig(): LiveData<Config?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(config: Config)

    @Update
    fun update(config: Config)

}