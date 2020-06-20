package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.codedirect.dermiscan.data.source.AppRepository
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Resource

class HomeVM (private val repository: AppRepository?) : ViewModel() {

    fun getImages() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository?.getImages()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}