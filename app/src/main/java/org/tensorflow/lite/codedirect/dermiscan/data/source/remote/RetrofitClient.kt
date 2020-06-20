package org.tensorflow.lite.codedirect.dermiscan.data.source.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.tensorflow.lite.codedirect.dermiscan.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient {
    companion object {
        val ok = OkHttpClient.Builder()
            .connectTimeout(50000, TimeUnit.SECONDS)
            .writeTimeout(50000, TimeUnit.SECONDS)
            .readTimeout(50000, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    fun response(): API {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val client = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(ok)
            .build()
        return client.create(API::class.java)
    }

    fun responseNew(): API {
        val gson = GsonBuilder()
                .setLenient()
                .create()

        val client = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_NEW)
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(ok)
            .build()
        return client.create(API::class.java)
    }
}