package com.anderson.orderapp.data.remote.datasource

import com.anderson.orderapp.data.remote.PizzaDto

interface PizzasRemoteDataSource {
    suspend fun fetchPizzas(): RemoteDataSourceResult<List<PizzaDto>>
}