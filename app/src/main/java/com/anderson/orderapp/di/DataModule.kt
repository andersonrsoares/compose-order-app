package com.anderson.orderapp.di

import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSource
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSourceImpl
import com.anderson.orderapp.domain.repository.PizzaRepositoryImpl
import com.anderson.orderapp.domain.repository.PizzasRepository
import org.koin.dsl.module

val DataModule = module {
    factory { PizzasRemoteDataSourceImpl(get()) as PizzasRemoteDataSource }

    factory { PizzaRepositoryImpl(get(), get()) as PizzasRepository }
}
