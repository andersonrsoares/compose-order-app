package com.anderson.orderapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import kotlinx.coroutines.flow.*
import java.math.BigDecimal


class CheckoutViewModel: ViewModel()  {

    private val _selectedPizzas = MutableStateFlow<List<Pizza>>(arrayListOf())

    val checkoutState =  _selectedPizzas.flatMapLatest {
        flow {
            emit(
                UiStateCheckout(
                    selectedPizzas = it,
                    totalValue = it.sumOf { it.price }.div(it.size)
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiStateCheckout()
    )

    fun confirm(){

    }

}

data class UiStateCheckout(
    val selectedPizzas: List<Pizza> = arrayListOf(),
    val totalValue: Double = 0.0,
    val confirmedOrder: Boolean = false
)