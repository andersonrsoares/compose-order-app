package com.anderson.orderapp.domain.repository

import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import kotlinx.coroutines.flow.Flow

interface PizzasRepository {
    fun fetchPizzas(): Flow<DataState<List<Pizza>>>
}