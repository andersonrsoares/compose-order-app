package com.anderson.orderapp.domain.repository

import com.anderson.orderapp.data.local.datasource.OrderLocalDataSource
import com.anderson.orderapp.domain.model.Pizza
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class OrderRepositoryImpl(
    private val orderLocalDataSource: OrderLocalDataSource
): OrderRepository {
    override fun save(pizzas: List<Pizza>): Flow<UUID> {
        return flow {
            emit(orderLocalDataSource.save(pizzas))
        }
    }

    override fun fetch(id: UUID): Flow<List<Pizza>> {
        return flow {
            emit(orderLocalDataSource.fetch(id))
        }
    }

    override fun clean(id: UUID): Flow<Boolean> {
        return flow {
            emit(orderLocalDataSource.clean(id))
        }
    }
}