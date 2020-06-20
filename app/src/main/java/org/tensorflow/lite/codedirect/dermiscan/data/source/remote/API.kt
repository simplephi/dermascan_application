package org.tensorflow.lite.codedirect.dermiscan.data.source.remote

import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("user/confirm")
    suspend fun usersLogin(
            @Field("username")username:String,
            @Field("password")password:String
    ): ResponseJSON

    @FormUrlEncoded
    @POST("user/create")
    suspend fun usersRegister(
            @Field("username")username:String,
            @Field("email")email:String,
            @Field("password")password:String
    ): ResponseJSON

    @GET("image/findAllImageAndroid")
    suspend fun getImages(): ResponseJSON

    @GET("user/getUserProfileById/{id}")
    suspend fun getUserProfile(
            @Path("id") id: String
    ): ResponseJSON

    @Multipart
    @POST("image/uploadAndroid")
    suspend fun uploadImage(
            @Part image: MultipartBody.Part,
            @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): ResponseJSON

    @Multipart
    @POST("image/uploadAndroid")
    suspend fun uploadImages(
            @Part image: MultipartBody.Part,
            @Part("title") title: RequestBody,
            @Part("label") label: RequestBody,
            @Part("user_id") user_id: RequestBody
    ): ResponseJSON

    @Multipart
    @POST("upload")
    suspend fun upload(
            @Part image: MultipartBody.Part
    ): ResponseJSON

}