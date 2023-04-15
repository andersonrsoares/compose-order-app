package com.anderson.orderapp.data.remote.datasource

import com.anderson.orderapp.data.remote.dto.PizzaDto
import com.anderson.orderapp.data.remote.result.RemoteDataSourceResult

interface PizzasRemoteDataSource {
    suspend fun fetchPizzas(): RemoteDataSourceResult<List<PizzaDto>>
}