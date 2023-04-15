package com.anderson.orderapp.data.remote.network

import com.anderson.orderapp.data.remote.PizzaDto
import retrofit2.Response
import retrofit2.http.GET


/**
 * Retrofit API Service
 */
interface OrderService {

    @GET("tests/pizzas.json")
    suspend fun pizzas() : Response<List<PizzaDto>>

}