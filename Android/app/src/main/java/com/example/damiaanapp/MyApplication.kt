package com.example.damiaanapp

import android.app.Application
import com.example.damiaanapp.DI.networkModule
import com.example.damiaanapp.DI.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// Authors: Joke Bergmans, Thibaud Steenhaut
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                networkModule,
                viewModelModule
            )
        }
    }
}