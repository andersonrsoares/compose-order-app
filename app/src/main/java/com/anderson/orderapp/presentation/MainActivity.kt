package com.anderson.orderapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anderson.orderapp.presentation.navigation.NavigationKeys
import com.anderson.orderapp.presentation.navigation.NavigationScreen
import com.anderson.orderapp.presentation.navigation.NavigationState
import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import com.anderson.orderapp.ui.theme.OrderappTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderappTheme {
                OrderApp()
            }
        }
    }
}

@Composable
private fun OrderApp() {
    val navController = rememberNavController()
    val navigationViewModel:NavigationViewModel = koinViewModel()
    Navigation(navController, navigationViewModel)

    NavHost(navController, startDestination = NavigationScreen.OrderPizza.route) {
        composable(route = NavigationScreen.OrderPizza.route) {
            //WeatherSearchDestination(navigationViewModel)
        }

        composable(
            route =  "${NavigationScreen.OrderPizzaConfirmation.route}/{${NavigationKeys.Arg.ORDER_PIZZA_CONFIRMATION}}" ,
            arguments = listOf(navArgument(NavigationKeys.Arg.ORDER_PIZZA_CONFIRMATION) {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { entry ->
//            WeatherDetailDestination(navController,
//                entry.arguments?.getString(NavigationKeys.Arg.CITY_NAME))
        }
    }
}

@Composable
fun Navigation(
    navController: NavController,
    navigationViewModel:NavigationViewModel
) {
    val navigation = navigationViewModel.navigation
    LaunchedEffect(navigation) {
        navigation.onEach  { route->
            when (route) {
                is NavigationState.PushNavigationState -> navController.navigate(route.destination)
                is NavigationState.PopNavigationState -> navController.popBackStack()
                else -> { print("do nothing") }
            }
        }.collect()
    }
}