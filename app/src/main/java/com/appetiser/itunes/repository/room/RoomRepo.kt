package com.appetiser.itunes.repository.room

import android.app.Application
import com.appetiser.itunes.data.Config
import com.appetiser.itunes.data.RoomEntity
import com.appetiser.itunes.data.Track
import com.appetiser.itunes.repository.room.db.AppDB
import com.appetiser.itunes.repository.room.db.dao.ConfigDao
import com.appetiser.itunes.repository.room.db.dao.TrackDao

/**
 * Repository for Room database.
 *
 * @param application   Application instance.
 * @param onCreated     Callback when DB is created.
 */
class RoomRepo(application: Application, onCreated: () -> Unit = {}) {

    private val _configDao: ConfigDao
    private val _trackDao: TrackDao

    init {
        val db = AppDB.getInstance(application, onCreated)

        _configDao = db.configDao()
        _trackDao = db.trackDao()
    }

    /**
     * Get [Config] data from database.
     *
     * @return [Config] LiveData.
     */
    fun getConfig() = _configDao.getConfig()

    /**
     * Get paged list of [Track] data from database.
     *
     * @return [Track] data source factory.
     */
    fun getTracks() = _trackDao.getTracks()

    /**
     * Insert data to database.
     *
     * @param data Data to insert.
     */
    fun insert(data: RoomEntity) {
        when(data) {
            is Config -> _configDao.insert(data)
        }
    }

    /**
     * Insert list of [Track] to database.
     *
     * @param tracks List of [Track] to insert.
     */
    fun insert(tracks: List<Track>) = _trackDao.insert(tracks)

    /**
     * Update data from database.
     *
     * @param data Data to update.
     */
    fun update(data: RoomEntity) {
        when(data) {
            is Config -> _configDao.update(data)
            is Track -> _trackDao.update(data)
        }
    }

    /**
     * Delete all data from database.
     *
     * @param javaClass Room entity/table to clear.
     */
    fun <T> deleteAll(javaClass: T) {
        if (javaClass == Track::javaClass) {
            _trackDao.deleteAll()
        }
    }

}