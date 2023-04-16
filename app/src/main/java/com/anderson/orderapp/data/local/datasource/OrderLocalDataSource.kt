package com.anderson.orderapp.data.local.datasource


import com.anderson.orderapp.domain.model.Pizza
import java.util.UUID

interface OrderLocalDataSource {
    suspend fun save(pizzas: List<Pizza>): UUID
    suspend fun fetch(id: UUID): List<Pizza>
    suspend fun clean(id: UUID): Boolean
}