package com.appetiser.itunes.repository.room.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.appetiser.itunes.data.Config
import com.appetiser.itunes.data.Track
import com.appetiser.itunes.repository.room.db.dao.ConfigDao
import com.appetiser.itunes.repository.room.db.dao.TrackDao

/**
 * Application database.
 */
@Database(
    entities = [
        Config::class,
        Track::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDB : RoomDatabase() {

    companion object {
        private var _instance: AppDB? = null

        /**
         * Get singleton instance of AppDB.
         *
         * @param application   Application intance.
         * @param onCreated     Callback when DB is created.
         * @return Singleton instance of this class.
         */
        @Synchronized
        fun getInstance(application: Application, onCreated: () -> Unit = {}): AppDB {
            _instance = Room.databaseBuilder(
                application.applicationContext,
                AppDB::class.java,
                "app_db"
            ).addCallback(object: RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    onCreated()
                }

            }).build()

            return _instance!!
        }
    }

    /**
     * Get Config DAO.
     *
     * @return ConfigDao
     */
    abstract fun configDao(): ConfigDao

    /**
     * Get Track DAO.
     *
     * @return TrackDao
     */
    abstract fun trackDao(): TrackDao

}