package com.anderson.orderapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.OrderRepository
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
import java.util.UUID


class CheckoutViewModel(
    private val orderRepository: OrderRepository
): ViewModel()  {

    private val _orderId = MutableStateFlow<UUID?>(null)
    private val _confirmedOrder = MutableStateFlow(false)

    val checkoutState =  combine(
        _orderId
            .filterNotNull()
            .flatMapLatest {
                orderRepository.fetch(it)
            },
        _confirmedOrder
    ) { selectedPizzas, confirmedOrder ->
        UiStateCheckout(
            selectedPizzas = selectedPizzas,
            totalValue = selectedPizzas.sumOf { it.price }.div(selectedPizzas.size),
            confirmedOrder = confirmedOrder
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiStateCheckout()
    )


    fun confirm() {
        _confirmedOrder.tryEmit(true)
    }

    suspend fun setOrderId(orderId: String) {
        _orderId.emit(UUID.fromString(orderId))
    }

}

data class UiStateCheckout(
    val selectedPizzas: List<Pizza> = arrayListOf(),
    val totalValue: Double = 0.0,
    val confirmedOrder: Boolean = false
)