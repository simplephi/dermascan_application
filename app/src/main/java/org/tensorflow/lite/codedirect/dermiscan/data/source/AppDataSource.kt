package org.tensorflow.lite.codedirect.dermiscan.data.source

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON

interface AppDataSource {
    suspend fun usersLogin(username:String, password:String): ResponseJSON
    suspend fun usersRegister(username: String, email: String, password: String): ResponseJSON
    suspend fun uploadImages(image: MultipartBody.Part, params: Map<String, @JvmSuppressWildcards RequestBody>): ResponseJSON
    suspend fun uploadImagess(image: MultipartBody.Part, title:RequestBody,label:RequestBody,userid:RequestBody): ResponseJSON
    suspend fun upload(image: MultipartBody.Part): ResponseJSON
    suspend fun getImages(): ResponseJSON
    suspend fun getUserProfile(id:String) : ResponseJSON
}