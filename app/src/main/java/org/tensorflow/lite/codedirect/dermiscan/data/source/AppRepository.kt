package org.tensorflow.lite.codedirect.dermiscan.data.source

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.RemoteRepository
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON

class AppRepository(
        private val remoteRepository: RemoteRepository
) : AppDataSource {

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(
                remoteRepository: RemoteRepository
        ): AppRepository = INSTANCE
                ?: synchronized(AppRepository::class.java) {
                    AppRepository(
                            remoteRepository
                    ).also {
                        INSTANCE = it
                    }
                }
    }

    override suspend fun usersLogin(username: String, password: String): ResponseJSON {
        return remoteRepository.loginUsers(username, password)
    }

    override suspend fun usersRegister(username: String, email: String, password: String): ResponseJSON {
        return remoteRepository.registerUsers(username, email, password)
    }

    override suspend fun uploadImages(image: MultipartBody.Part, params: Map<String, RequestBody>): ResponseJSON {
        return remoteRepository.uploadImages(image, params)
    }

    override suspend fun uploadImagess(image: MultipartBody.Part, title: RequestBody, label: RequestBody, userid: RequestBody): ResponseJSON {
        return remoteRepository.uploadImagess(image, title, label, userid)
    }

    override suspend fun upload(image: MultipartBody.Part): ResponseJSON {
        return remoteRepository.upload(image)
    }

    override suspend fun getImages(): ResponseJSON {
        return remoteRepository.getImages()
    }

    override suspend fun getUserProfile(id: String): ResponseJSON {
        return remoteRepository.getUserProfile(id)
    }

}