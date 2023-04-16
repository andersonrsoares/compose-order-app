package com.anderson.orderapp.data.local.datasource


import com.anderson.orderapp.domain.model.Pizza
import java.util.*


class OrderLocalDataSourceImpl(): OrderLocalDataSource {

    companion object {
        @JvmStatic
        private val cachedData = hashMapOf<UUID, List<Pizza>>()
        get() =
    }

    override suspend fun save(pizzas: List<Pizza>): UUID {
        val id = UUID.randomUUID()
        cachedData[id] = pizzas
        return id
    }

    override suspend fun fetch(id: UUID): List<Pizza> {
       return cachedData.getOrDefault(id, arrayListOf())
    }

    override suspend fun clean(id: UUID): Boolean {
        if(cachedData.containsKey(id)) {
            cachedData.remove(id)
            return true
        }
        return false
    }
}