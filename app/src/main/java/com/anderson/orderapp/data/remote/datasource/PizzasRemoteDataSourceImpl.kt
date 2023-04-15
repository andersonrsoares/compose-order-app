package com.anderson.orderapp.data.remote.datasource

import com.anderson.orderapp.data.remote.network.OrderService
import com.anderson.orderapp.data.remote.PizzaDto

class PizzasRemoteDataSourceImpl(
    private val service: OrderService
): PizzasRemoteDataSource {
    override suspend fun fetchPizzas(): RemoteDataSourceResult<List<PizzaDto>> {
        return safeApiCall {
            service.pizzas()
        }
    }
}