@file:OptIn(ExperimentalMaterial3Api::class)
package com.anderson.orderapp.presentation.checkout


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.presentation.components.ShowMessage
import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import org.koin.androidx.compose.koinViewModel



@Composable
fun CheckoutScreen(
    navigationViewModel: NavigationViewModel = koinViewModel(key = "navigate"),
    checkoutViewModel: CheckoutViewModel = koinViewModel(),
    args: List<Pizza>
) {

    val pizzaMenuState by checkoutViewModel.checkoutState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            if (pizzaMenuState.confirmedOrder.not()) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            navigationViewModel.pop()
                        }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.arrow_back_content_description)
                            )
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.pizza_title_checkout))
                    },
                )
            }
        }
    ) {
        CheckoutBody(
            checkoutState = pizzaMenuState,
            paddingValues = it,
            onConfirm = {
                checkoutViewModel.confirm()
            }
        )
    }
}
@Composable
fun CheckoutBody(
    checkoutState: UiStateCheckout,
    paddingValues: PaddingValues,
    onConfirm: (() -> Unit),
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding())) {
                when {
                    checkoutState.confirmedOrder ->  {
                        ShowMessage(message = stringResource(id = R.string.order_confirmed_with_success))
                    }
                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(modifier = Modifier
                                .padding(12.dp)
                                .fillMaxSize()
                            ) {
                                Row {
                                    checkoutState.selectedPizzas.forEach {item->
                                        Text(
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            text = item.name)
                                        Text(
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            text = item.price.toString())
                                    }
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        fontSize = 30.sp,
                                        color = Color.Black,
                                        text = checkoutState.totalValue.toString())
                                }
                            }
                            Button(
                                onClick = {
                                    onConfirm()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.pizza_checkout_confirm))
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
                    Row {
                        Checkbox(
                            checked = selectedItems.contains(item),
                            onCheckedChange = {
                                onSelectPizza(item)
                            }
                        )
                        Text(
                            fontSize = 15.sp,
                            color = Color.Black,
                            text = item.name)
                        Text(
                            fontSize = 15.sp,
                            color = Color.Black,
                            text = item.price.toString())
                    }
                }
            }
        }
    }
}
