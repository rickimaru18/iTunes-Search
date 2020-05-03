package com.appetiser.itunes.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.appetiser.itunes.data.Config
import com.appetiser.itunes.data.Track
import com.appetiser.itunes.repository.retrofit.RetrofitRepo
import com.appetiser.itunes.repository.room.RoomRepo
import kotlinx.coroutines.*
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

/**
 * Repository entry point.
 */
class AppRepo {

    companion object {
        private var _instance: AppRepo? = null

        /**
         * Get singleton instance of [AppRepo].
         *
         * @param application Application instance.
         * @return Singleton instance of this class.
         */
        @Synchronized
        fun getInstance(application: Application? = null): AppRepo {
            if (_instance == null) {
                _instance = AppRepo(application)
            }
            return  _instance!!
        }
    }

    /**
     * Media types when searching to iTunes.
     */
    enum class MediaType {
        movie,
        podcast,
        music,
        musicVideo,
        audiobook,
        shortFilm,
        tvShow,
        software,
        ebook,
        all
    }

    private val _retrofitRepo = RetrofitRepo()
    private var _roomRepo: RoomRepo? = null

    private val _isFetchingUpdatedTracks = MutableLiveData<Boolean>(false)

    /**
     * A boolean LiveData if repository is currently fetching updated data from iTunes.
     */
    val isFetchingUpdatedTracks = _isFetchingUpdatedTracks

    private constructor(application: Application?) {
        if (application != null) {
            _roomRepo = RoomRepo(application) {
                GlobalScope.launch(Dispatchers.IO) {
                    searchTracks()
                }
            }
        }
    }

    /**
     * Search tracks from iTunes.
     *
     * @param term  Term to search.
     * @param media Media type to search, refer to [MediaType].
     * @return List<Track>
     */
    suspend fun searchTracks(
        term: String = "*",
        media: MediaType = MediaType.all
    ): List<Track> {
        _isFetchingUpdatedTracks.postValue(true)
        val searchResult = _retrofitRepo.searchTracks(term, Locale.getDefault().country, media.name)

        if (searchResult != null) {
            _roomRepo?.let {
                it.deleteAll(Track::javaClass)
                it.insert(searchResult.tracks)

                val dateTime = LocalDateTime.now()
                val lastUpdated = StringBuilder()
                    .append(dateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                    .append(" ")
                    .append(dateTime.dayOfMonth)
                    .append(", ")
                    .append(dateTime.year)
                    .append(" -- ")
                    .append(if (dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour)
                    .append(":")
                    .append(if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute)
                    .toString()

                // Insert/Replace config data to database
                it.insert(Config(
                    if (term == "*") null else term,
                    media.name,
                    lastUpdated
                ))
            }
        }

        _isFetchingUpdatedTracks.postValue(false)
        return searchResult.tracks
    }

    /**
     * Get [Config] data from database.
     *
     * @return [Config] LiveData.
     */
    fun getConfig() = _roomRepo?.getConfig()

    /**
     * Get paged list of [Track] data from database.
     *
     * @return [Track] data source factory.
     */
    fun getTracks() = _roomRepo?.getTracks()

    /**
     * Update [Track] data from database.
     *
     * @param track [Track] data to update.
     */
    fun update(track: Track) = _roomRepo?.update(track)

}