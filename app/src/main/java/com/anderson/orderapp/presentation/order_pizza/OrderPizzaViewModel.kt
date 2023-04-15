package com.anderson.orderapp.presentation.order_pizza

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OrderPizzaViewModel(
    pizzasRepository: PizzasRepository
): ViewModel()  {

    private val _pizzas = MutableStateFlow<UiStateOrderPizza>(UiStateOrderPizza.Loading)
    val pizzas = _pizzas.asStateFlow()


    init {
        pizzasRepository.fetchPizzas()
            .onEach {
                when {
                    it is DataState.Success && it.data.isNotEmpty() -> _pizzas.tryEmit(UiStateOrderPizza.Success(it.data))
                    it is DataState.Success && it.data.isEmpty() -> _pizzas.tryEmit(UiStateOrderPizza.Empty(UiText.ResourceString(R.string.empty_pizzas_list)))
                    it is DataState.Loading -> _pizzas.tryEmit(UiStateOrderPizza.Loading)
                    it is DataState.Failure -> _pizzas.tryEmit(UiStateOrderPizza.Failure(UiText.ResourceString(it.reason.defaultResourceMessage())))
                }
            }.launchIn(viewModelScope)
    }
}

sealed class UiStateOrderPizza  {
    class Success(val data: List<Pizza>): UiStateOrderPizza()
    object Loading : UiStateOrderPizza()
    class Empty(val message: UiText): UiStateOrderPizza()
    class Failure(val error: UiText): UiStateOrderPizza()
}