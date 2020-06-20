package org.tensorflow.lite.codedirect.dermiscan.di

import org.koin.dsl.module
import org.tensorflow.lite.codedirect.dermiscan.data.source.AppRepository
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.RemoteRepository
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.RetrofitClient
import org.tensorflow.lite.codedirect.dermiscan.ui.login.LoginVM
import org.tensorflow.lite.codedirect.dermiscan.ui.main.features.add.AddVM
import org.tensorflow.lite.codedirect.dermiscan.ui.main.features.home.HomeVM
import org.tensorflow.lite.codedirect.dermiscan.ui.main.features.profile.ProfileVM
import org.tensorflow.lite.codedirect.dermiscan.ui.register.RegisterVM

object AppModule {
    val view_models = module {
        single { LoginVM(get()) }
        single { ProfileVM(get()) }
        single { RegisterVM(get()) }
        single { AddVM(get()) }
        single { HomeVM(get()) }
    }


    val repositoryModule = module {
        fun provideRepository(): AppRepository? {
            val remoteRepository =
                    RemoteRepository.getInstance(RetrofitClient())
            return remoteRepository.let { localrepo ->
                AppRepository.getInstance(remoteRepository)
            }
        }
        single { provideRepository() }
    }
}