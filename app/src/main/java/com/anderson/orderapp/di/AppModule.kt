package com.anderson.orderapp.di

import com.anderson.orderapp.DispatcherProvider
import org.koin.dsl.module

val AppModule = module {
    single { DispatcherProvider.StandardDispatchers() as DispatcherProvider }
}