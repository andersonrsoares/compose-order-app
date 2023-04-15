package com.anderson.orderapp.presentation.navigation

sealed class NavigationScreen(val route: String) {
    object OrderPizza : NavigationScreen("ORDER_PIZZA")
    object OrderPizzaConfirmation : NavigationScreen("ORDER_PIZZA_CONFIRMATION") {
        fun args(param: String?) = param?.let { listOf<Any>(NavigationKeys.Arg.ORDER_PIZZA_CONFIRMATION to param) } ?: listOf()
    }
    override fun toString(): String = route
}
internal object NavigationKeys {
    object Arg {
        const val ORDER_PIZZA_CONFIRMATION = "ORDER_PIZZA_CONFIRMATION"
    }
}

