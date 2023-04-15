package com.anderson.orderapp.di

import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSource
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSourceImpl
import org.koin.dsl.module

val RepositoryModule = module {
    single { PizzasRemoteDataSourceImpl(get()) as PizzasRemoteDataSource }
}
