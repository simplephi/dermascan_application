package org.tensorflow.lite.codedirect.dermiscan.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.tensorflow.lite.codedirect.dermiscan.data.source.AppRepository
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Resource
import kotlinx.coroutines.Dispatchers

class LoginVM(private val repository: AppRepository?) : ViewModel() {

    fun getUsers(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository?.usersLogin(username, password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}