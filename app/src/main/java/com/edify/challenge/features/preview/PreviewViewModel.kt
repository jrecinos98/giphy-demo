package com.edify.challenge.features.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edify.challenge.R
import com.edify.challenge.domain.GifRepository
import com.giphy.sdk.core.models.Media
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PreviewViewModel @Inject constructor(
    private val gifRepository: GifRepository
) : ViewModel() {

    private var _errorObs = MutableLiveData<Int>()
    val errorObs : LiveData<Int>
        get() {
            _errorObs = MutableLiveData<Int>()
            return _errorObs
        }

    private val exceptionHandler : CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e(throwable)
        _errorObs.postValue(R.string.save_failure)
    }

    /**
     * Saves GIF to Phone external storage / Gallery
     */
    fun saveGif(gifUrl : String, gifId : String){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            gifRepository.saveGif(gifUrl, gifId)
            _errorObs.postValue(R.string.save_success)
        }
    }

}