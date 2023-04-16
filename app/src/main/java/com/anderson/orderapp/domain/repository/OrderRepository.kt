package com.anderson.orderapp.domain.repository


import com.anderson.orderapp.domain.model.Pizza
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface OrderRepository {
    fun save(pizzas: List<Pizza>): Flow<UUID>
    fun fetch(id: UUID): Flow<List<Pizza>>
    fun clean(id: UUID): Flow<Boolean>
}