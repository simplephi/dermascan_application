package org.tensorflow.lite.codedirect.dermiscan.di

import android.app.Application
import org.tensorflow.lite.codedirect.dermiscan.di.AppModule.repositoryModule
import org.tensorflow.lite.codedirect.dermiscan.di.AppModule.view_models
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(view_models, repositoryModule))
        }
    }

}