@file:OptIn(ExperimentalMaterial3Api::class)
package com.anderson.orderapp.presentation.pizza_menu


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.presentation.asMoney
import com.anderson.orderapp.presentation.asString
import com.anderson.orderapp.presentation.components.LoadingBar
import com.anderson.orderapp.presentation.components.ShowMessage
import com.anderson.orderapp.presentation.getString
import com.anderson.orderapp.presentation.navigation.NavigationScreen
import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import kotlinx.coroutines.flow.*
import org.koin.androidx.compose.koinViewModel



@Composable
fun OrderPizzaScreen(
    pizzaMenuViewModel: PizzaMenuViewModel = koinViewModel(),
    navigationViewModel: NavigationViewModel = koinViewModel(),
) {

    val context = LocalContext.current
    val pizzaMenuState by pizzaMenuViewModel.pizzaMenuState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = pizzaMenuViewModel.goToCheckout){
        pizzaMenuViewModel.goToCheckout.collectLatest {
            navigationViewModel.push(NavigationScreen.CheckoutOrder, args = listOf(it.toString()))
        }
    }

    LaunchedEffect(key1 = pizzaMenuViewModel.toastMessage){
        pizzaMenuViewModel.toastMessage.collectLatest {
            snackbarHostState.showSnackbar(
                it.getString(context)
            )
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.pizza_menu_title))
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        OrderPizzaBody(
            orderPizzaState = pizzaMenuState,
            onSelectPizza = {
                pizzaMenuViewModel.selectPizza(it)
            },
            onCheckout = {
                pizzaMenuViewModel.checkout()
            }
        )
    }
}
@Composable
fun OrderPizzaBody(
    orderPizzaState: UiStatePizzaMenu,
    onSelectPizza: ((Pizza) -> Unit),
    onCheckout: (() -> Unit),
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 50.dp, bottom = 5.dp)) {
                when {
                    orderPizzaState.isLoading -> LoadingBar()
                    orderPizzaState.errorMessage != null -> ShowMessage(orderPizzaState.errorMessage.asString())
                    orderPizzaState.pizzas.isEmpty() -> ShowMessage(orderPizzaState.emptyMessage.asString())
                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PizzaList(
                                pizzaItems = orderPizzaState.pizzas,
                                selectedItems = orderPizzaState.selectedPizzas,
                                onSelectPizza = onSelectPizza
                            )
                            Button(
                                onClick = {
                                    onCheckout()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.pizza_menu_checkout))
                            }
                        }

                    }
                }
            }
}


@Composable
fun PizzaList(
    pizzaItems: List<Pizza>,
    selectedItems: List<Pizza>,
    onSelectPizza: ((Pizza) -> Unit),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pizzaItems) { item ->
            Box {
                Box(modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedItems.contains(item),
                            onCheckedChange = {
                                onSelectPizza(item)
                            }
                        )
                        Text(
                            fontSize = 15.sp,
                            color = Color.Black,
                            text = "${item.name}  ${item.price.asMoney()}")
                    }
                }
            }
        }
    }
}
