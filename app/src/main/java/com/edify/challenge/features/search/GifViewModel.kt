package com.edify.challenge.features.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class GifViewModel @Inject constructor() : ViewModel() {

    private val _searchObs = MutableLiveData<String>()

    val searchObs : LiveData<String>
        get() {
           return _searchObs
        }

    fun searchGif(searchTerm : String){
        _searchObs.postValue(searchTerm)
    }

}