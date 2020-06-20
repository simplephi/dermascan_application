package org.tensorflow.lite.codedirect.dermiscan.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.tensorflow.lite.codedirect.dermiscan.data.source.AppRepository
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Resource
import kotlinx.coroutines.Dispatchers

class RegisterVM (private val repository: AppRepository?) : ViewModel() {

    fun usersRegister(username: String, email: String, password: String)= liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository?.usersRegister(username, email, password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}