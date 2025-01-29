package com.example.cinephile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cinephile.model.ShowModel
import com.example.cinephile.repository.ShowRepository

class ShowViewModel(val repository: ShowRepository) {

    fun addShow(showModel: ShowModel, callback: (Boolean, String) -> Unit) {
        repository.addShows(showModel, callback)
    }

    fun updateShow(showId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repository.updateShow(showId, data, callback)
    }

    fun deleteShow(showId: String, callback: (Boolean, String) -> Unit) {
        repository.deleteShow(showId, callback)
    }

    var _show = MutableLiveData<ShowModel?>()
    var show = MutableLiveData<ShowModel?>()
        get() = _show

    var _allShows = MutableLiveData<List<ShowModel>?>()
    var allShows = MutableLiveData<List<ShowModel>?>()
        get() = _allShows

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun getAllShows() {
        _loadingState.value = true
        repository.getAllShows { shows, success, message ->
            if (success) {
                _allShows.value = shows
                _loadingState.value = false
            }
        }
    }
}
