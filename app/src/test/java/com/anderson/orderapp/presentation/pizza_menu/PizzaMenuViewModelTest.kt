@file:OptIn(ExperimentalCoroutinesApi::class)
package com.anderson.orderapp.presentation.pizza_menu


import com.anderson.orderapp.R
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.FailureReason
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.OrderRepository
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.UiText
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Test
import java.util.*


class PizzaMenuViewModelTest {
    private val orderRepository = mockk<OrderRepository>()

    @Test
    fun `test load data success`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            val pizzasRepository =  mockk<PizzasRepository>()
            every { pizzasRepository.fetchPizzas() } returns flow {
                emit(DataState.Success(arrayListOf(Pizza("test", 1.10))))
            }

            val viewModel = PizzaMenuViewModel(
                orderRepository = orderRepository,
                pizzasRepository = pizzasRepository
            )
            viewModel.pizzaMenuState.collect()

            assert(viewModel.pizzaMenuState.value == UiStatePizzaMenu(
                pizzas = arrayListOf(Pizza("test", 1.10)),
                isLoading = false
            ))

            verify (
                exactly = 1
            ) {
                pizzasRepository.fetchPizzas()
            }

            verify (
                exactly = 0
            ) {
                orderRepository.save(any())
            }
        }
    }

    @Test
    fun `test load data error`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            val pizzasRepository =  mockk<PizzasRepository>()
            every { pizzasRepository.fetchPizzas() } returns flow {
                emit(DataState.Failure(FailureReason.NetworkIssue))
            }

            val viewModel = PizzaMenuViewModel(
                orderRepository = orderRepository,
                pizzasRepository = pizzasRepository
            )
            viewModel.pizzaMenuState.collect()

            assert(viewModel.pizzaMenuState.value == UiStatePizzaMenu(
                errorMessage = UiText.ResourceString(R.string.error_network_issue),
                isLoading = false
            ))

            verify (
                exactly = 1
            ) {
                pizzasRepository.fetchPizzas()
            }

            verify (
                exactly = 0
            ) {
                orderRepository.save(any())
            }
        }
    }
}