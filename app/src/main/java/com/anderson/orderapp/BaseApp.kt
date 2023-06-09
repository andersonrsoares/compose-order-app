package com.anderson.orderapp

import android.app.Application
import com.anderson.orderapp.di.AppModule
import com.anderson.orderapp.di.DataModule
import com.anderson.orderapp.di.NetworkModule
import com.anderson.orderapp.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApp)
            modules(listOf(AppModule, NetworkModule, DataModule, ViewModelModule))
        }
    }
}