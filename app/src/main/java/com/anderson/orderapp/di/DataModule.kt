package com.anderson.orderapp.di

import com.anderson.orderapp.data.local.datasource.OrderLocalDataSource
import com.anderson.orderapp.data.local.datasource.OrderLocalDataSourceImpl
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSource
import com.anderson.orderapp.data.remote.datasource.PizzasRemoteDataSourceImpl
import com.anderson.orderapp.domain.repository.OrderRepository
import com.anderson.orderapp.domain.repository.OrderRepositoryImpl
import com.anderson.orderapp.domain.repository.PizzaRepositoryImpl
import com.anderson.orderapp.domain.repository.PizzasRepository
import org.koin.dsl.module

val DataModule = module {
    factory { PizzasRemoteDataSourceImpl(get()) as PizzasRemoteDataSource }

    factory { PizzaRepositoryImpl(get(), get()) as PizzasRepository }

    factory { OrderLocalDataSourceImpl() as OrderLocalDataSource }

    factory { OrderRepositoryImpl(get()) as OrderRepository }
}
