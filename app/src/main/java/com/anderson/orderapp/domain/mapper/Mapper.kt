package com.anderson.orderapp.domain.mapper

import com.anderson.orderapp.data.remote.dto.PizzaDto
import com.anderson.orderapp.domain.model.Pizza


fun PizzaDto.toPizza() : Pizza =
    Pizza(
        name = this.name,
        price = this.price
    )




