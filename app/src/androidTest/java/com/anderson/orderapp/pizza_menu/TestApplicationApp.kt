package com.anderson.orderapp.pizza_menu

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


class TestApplicationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}