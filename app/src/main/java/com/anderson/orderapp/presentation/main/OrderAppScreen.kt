
package com.anderson.orderapp.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anderson.orderapp.presentation.checkout.CheckoutScreen
import com.anderson.orderapp.presentation.navigation.NavigationKeys
import com.anderson.orderapp.presentation.navigation.NavigationScreen
import com.anderson.orderapp.presentation.navigation.NavigationState
import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import com.anderson.orderapp.presentation.pizza_menu.OrderPizzaScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderAppScreen() {
    val navController = rememberNavController()
    val navigationViewModel: NavigationViewModel = koinViewModel(key = "navigate")
    Navigation(navController, navigationViewModel)

    NavHost(navController, startDestination = NavigationScreen.PizzaMenu.route) {
        composable(route = NavigationScreen.PizzaMenu.route) {
            OrderPizzaScreen(
                navigationViewModel = navigationViewModel
            )
        }

        composable(
            route =  "${NavigationScreen.CheckoutOrder.route}/{${NavigationKeys.Arg.CHECKOUT_ORDER_ID}}" ,
            arguments = listOf(navArgument(NavigationKeys.Arg.CHECKOUT_ORDER_ID) {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { entry ->
            CheckoutScreen(
                navigationViewModel = navigationViewModel,
                orderId = entry.arguments?.getString(NavigationKeys.Arg.CHECKOUT_ORDER_ID)!!
            )
        }
    }
}

@Composable
fun Navigation(
    navController: NavController,
    navigationViewModel: NavigationViewModel
) {
    val navigation = navigationViewModel.navigation
    LaunchedEffect(navigation) {
        navigation.onEach  { route->
            when (route) {
                is NavigationState.PushNavigationState -> navController.navigate(route.destination)
                is NavigationState.PopNavigationState -> navController.popBackStack()
                is NavigationState.CleanNavigationState -> {
                    navController.backQueue.clear()
                    navController.navigate(
                        route = route.destination
                    )
                }
                else -> { print("do nothing") }
            }
        }.collect()
    }
}
