package org.tensorflow.lite.codedirect.dermiscan.data.source.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteRepository(private val apiService: RetrofitClient) {
    companion object {
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(apiService: RetrofitClient): RemoteRepository {
            if (INSTANCE == null) {
                INSTANCE = RemoteRepository(apiService)
            }
            return INSTANCE as RemoteRepository
        }
    }

    suspend fun loginUsers(username: String, password: String) = apiService.response().usersLogin(username, password)
    suspend fun registerUsers(username: String, email: String, password: String) = apiService.response().usersRegister(username, email, password)
    suspend fun uploadImages(image: MultipartBody.Part, params: Map<String, @JvmSuppressWildcards RequestBody>) = apiService.response().uploadImage(image, params)
    suspend fun uploadImagess(image: MultipartBody.Part, title:RequestBody,label:RequestBody,userid:RequestBody) = apiService.response().uploadImages(image,title,label,userid)
    suspend fun upload(image: MultipartBody.Part) = apiService.responseNew().upload(image)
    suspend fun getImages() = apiService.response().getImages()
    suspend fun getUserProfile(id:String) = apiService.response().getUserProfile(id)

}