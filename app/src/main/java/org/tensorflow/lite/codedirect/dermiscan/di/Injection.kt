package com.codedirect.audiometer.di

import android.app.Application
import org.tensorflow.lite.codedirect.dermiscan.data.source.AppRepository
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.RemoteRepository
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.RetrofitClient

class Injection {
    companion object {
        fun provideRepository(application: Application?): AppRepository? {
            val remoteRepository = RemoteRepository.getInstance(RetrofitClient())
            return remoteRepository.let {
                AppRepository.getInstance(remoteRepository)
            }
        }
    }
}