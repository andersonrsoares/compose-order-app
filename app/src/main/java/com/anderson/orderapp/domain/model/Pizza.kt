package com.anderson.orderapp.domain.model

import com.anderson.orderapp.data.remote.dto.PizzaDto

class PizzaList: ArrayList<PizzaDto>()
data class Pizza (
    val name: String,
    val price: Double
)