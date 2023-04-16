package com.anderson.orderapp.presentation.navigation

import com.anderson.orderapp.domain.model.Pizza

sealed class NavigationScreen(val route: String) {
    object PizzaMenu : NavigationScreen("PIZZA_MENU")
    object CheckoutOrder : NavigationScreen("CHECKOUT_ORDER") {
        fun args(param: List<Pizza>?) = param?.let { listOf<Any>(NavigationKeys.Arg.CHECKOUT_ORDER to param) } ?: listOf()
    }
    override fun toString(): String = route
}
internal object NavigationKeys {
    object Arg {
        const val CHECKOUT_ORDER = "CHECKOUT_ORDER"
    }
}

