package com.anderson.orderapp.presentation.pizza_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.orderapp.DispatcherProvider
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.OrderRepository
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID


class PizzaMenuViewModel(
    pizzasRepository: PizzasRepository,
    private val orderRepository: OrderRepository,
): ViewModel()  {

    private val _selectedPizzas = MutableStateFlow<MutableList<Pizza>>(arrayListOf())

    private val _toastMessage = MutableSharedFlow<UiText>(extraBufferCapacity = 1)
    val toastMessage = _toastMessage.asSharedFlow()

    private val _goToCheckout = MutableSharedFlow<UUID>(extraBufferCapacity = 1)
    val goToCheckout = _goToCheckout.asSharedFlow()

    val pizzaMenuState =  combine(
        pizzasRepository.fetchPizzas(),
        _selectedPizzas,
    ) { dataPizzas, selectedPizzas->
        when (dataPizzas) {
            is DataState.Success -> UiStatePizzaMenu(
                pizzas = dataPizzas.data,
                selectedPizzas = selectedPizzas,
                isLoading = false
            )
            is DataState.Loading -> UiStatePizzaMenu(
                isLoading = true
            )
            is DataState.Failure -> UiStatePizzaMenu(
                isLoading = false,
                errorMessage = UiText.ResourceString(dataPizzas.failureReason.defaultResourceMessage())
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiStatePizzaMenu()
    )

    fun selectPizza(pizza: Pizza){
        if (_selectedPizzas.value.contains(pizza)) {
            unselectPizza(pizza)
            return
        }

        if(_selectedPizzas.value.size >= 2) {
            viewModelScope.launch {
                _toastMessage.tryEmit(UiText.ResourceString(R.string.pizza_max_flavor))
            }
            return
        }

        _selectedPizzas.tryEmit(_selectedPizzas.value.toMutableList().apply {
            this.add(pizza)
        })
    }

    private fun unselectPizza(pizza: Pizza){
        _selectedPizzas.tryEmit(_selectedPizzas.value.toMutableList().apply {
            this.remove(pizza)
        })
    }

    fun checkout(){
        if(_selectedPizzas.value.isEmpty()) {
            viewModelScope.launch {
                _toastMessage.emit(UiText.ResourceString(R.string.pizza_not_selected_flavor))
            }
            return
        }
        orderRepository
            .save(pizzas = _selectedPizzas.value)
            .onEach  {
                viewModelScope.launch {
                    _goToCheckout.emit(it)
                }
            }.launchIn(viewModelScope)
    }
}

data class UiStatePizzaMenu(
    val pizzas: List<Pizza> = arrayListOf(),
    val selectedPizzas: List<Pizza> = arrayListOf(),
    val isLoading:Boolean = true,
    val emptyMessage: UiText = UiText.ResourceString(R.string.empty_pizzas_list),
    val errorMessage: UiText? = null,
)