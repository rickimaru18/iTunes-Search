package com.appetiser.itunes.ui.home

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import com.appetiser.itunes.data.Config
import com.appetiser.itunes.data.Track
import com.appetiser.itunes.repository.AppRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _appRepo = AppRepo.getInstance(application)

    /**
     * Get [Config] data from repository.
     *
     * @return [Config] LiveData.
     */
    val config = _appRepo.getConfig()

    /**
     * A boolean LiveData if repository is currently fetching updated data from iTunes.
     */
    val isFetchingUpdatedTracks = _appRepo.isFetchingUpdatedTracks

    /**
     * Get paged list of [Track] data from repository.
     *
     * @return [Track] LiveData for paged list.
     */
    val tracks = LivePagedListBuilder(
        _appRepo.getTracks()!!, 5
    ).build()

    /**
     * Search tracks from iTunes.
     * To get tracks, observe [tracks] LiveData.
     *
     * @param term  Term to search.
     * @param media Media type.
     */
    fun searchTracks(
        term: String = "*",
        media: String? = null
    ) = viewModelScope.launch(Dispatchers.IO) {
        var mediaType = AppRepo.MediaType.all
        if (media != null) {
            val mediaTmp = media.decapitalize().replace(" ", "")
            mediaType = AppRepo.MediaType.valueOf(mediaTmp)
        }
        _appRepo.searchTracks(term, mediaType)
    }

    /**
     * Toggle [Track.isSelected], and update repository.
     *
     * @param track Track data.
     */
    fun toggleSelectedTrack(track: Track) = viewModelScope.launch(Dispatchers.IO) {
        track.isSelected = !track.isSelected
        _appRepo.update(track)
    }

}
