package com.anderson.orderapp.presentation.pizza_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import kotlinx.coroutines.flow.*


class PizzaMenuViewModel(
    pizzasRepository: PizzasRepository
): ViewModel()  {

    private val _selectedPizzas = MutableStateFlow<MutableList<Pizza>>(arrayListOf())

    private val _toastMessage = MutableStateFlow<UiText?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    private val _goToCheckout = MutableStateFlow<MutableList<Pizza>?>(null)
    val goToCheckout = _goToCheckout.asStateFlow()


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
            _toastMessage.tryEmit(UiText.ResourceString(R.string.pizza_max_flavor))
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
            _toastMessage.tryEmit(UiText.ResourceString(R.string.pizza_not_selected_flavor))
            return
        }
        _goToCheckout.tryEmit(arrayListOf())
        _goToCheckout.tryEmit(_selectedPizzas.value)
    }
}

data class UiStatePizzaMenu(
    val pizzas: List<Pizza> = arrayListOf(),
    val selectedPizzas: List<Pizza> = arrayListOf(),
    val isLoading:Boolean = true,
    val emptyMessage: UiText = UiText.ResourceString(R.string.empty_pizzas_list),
    val errorMessage: UiText? = null,
    val selectedMaxFlavors: UiText = UiText.ResourceString(R.string.empty_pizzas_list)
)