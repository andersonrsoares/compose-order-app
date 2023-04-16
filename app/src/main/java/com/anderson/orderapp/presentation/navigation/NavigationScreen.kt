package com.anderson.orderapp.presentation.navigation

import com.anderson.orderapp.domain.model.Pizza

sealed class NavigationScreen(val route: String) {
    object PizzaMenu : NavigationScreen("PIZZA_MENU")
    object OrderPizzaConfirmation : NavigationScreen("ORDER_PIZZA_CONFIRMATION") {
        fun args(param: List<Pizza>?) = param?.let { listOf<Any>(NavigationKeys.Arg.ORDER_PIZZA_CONFIRMATION to param) } ?: listOf()
    }
    override fun toString(): String = route
}
internal object NavigationKeys {
    object Arg {
        const val ORDER_PIZZA_CONFIRMATION = "ORDER_PIZZA_CONFIRMATION"
    }
}

